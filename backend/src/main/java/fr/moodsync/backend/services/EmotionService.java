package fr.moodsync.backend.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fr.moodsync.backend.models.EmotionResponse;
import fr.moodsync.backend.models.Movie;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

@Service
public class EmotionService {

    private static final String LLM_API_URL = "http://localhost:1234/v1/chat/completions"; // Remplace par ton URL
    private static final String PYTHON_SCRIPT_PATH = "./src/main/java/fr/moodsync/backend/python/emotion_analysis.py";
    public EmotionResponse analyzeEmotion(String text) {
        try {
            // Vérifier si le fichier Python existe
            File scriptFile = new File(PYTHON_SCRIPT_PATH);
            if (!scriptFile.exists() || !scriptFile.isFile()) {
                return new EmotionResponse("error", "Python script not found", new ArrayList<>());
            }

            // Exécuter le script Python
            ProcessBuilder processBuilder = new ProcessBuilder(List.of("python", PYTHON_SCRIPT_PATH, text));
            Process process = processBuilder.start();

            // Lire la sortie du script Python
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String response = reader.lines().collect(Collectors.joining("\n"));

            // Vérifier si la réponse est vide
            if (response == null || response.trim().isEmpty()) {
                return new EmotionResponse("error", "empty response from Python script", new ArrayList<>());
            }

            // Parser le JSON avec Jackson
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response);

            String emotion = jsonNode.has("emotion") ? jsonNode.get("emotion").asText() : "unknown";

            // Appel à l'API Mistral pour générer la suggestion d'activité
            String suggestion = getActivitySuggestion(text);

            // Convertir la liste de films
            List<Movie> movies = new ArrayList<>();
            if (jsonNode.has("movies")) {
                for (JsonNode movieNode : jsonNode.get("movies")) {
                    Movie movie = new Movie(
                            movieNode.get("title").asText(),
                            movieNode.get("overview").asText(),
                            movieNode.get("poster_path").asText(),
                            movieNode.get("vote_average").asDouble(),
                            movieNode.get("release_date").asText()
                    );
                    movies.add(movie);
                }
            }

            return new EmotionResponse(emotion, suggestion, movies);

        } catch (Exception e) {
            e.printStackTrace();
            return new EmotionResponse("error", "exception occurred - " + e.getMessage(), new ArrayList<>());
        }
    }

    private String getActivitySuggestion(String userText) {
        try {
            // Création du corps de la requête JSON de manière plus sûre
            ObjectMapper objectMapper = new ObjectMapper();
            ObjectNode rootNode = objectMapper.createObjectNode();
            rootNode.put("model", "mathstral-7b-v0.1");

            // Création du tableau de messages
            ArrayNode messagesNode = objectMapper.createArrayNode();
            ObjectNode systemMessageNode = objectMapper.createObjectNode();
            systemMessageNode.put("role", "system");
            systemMessageNode.put("content", "You are an empathetic and supportive AI that helps users by suggesting activities tailored to their emotional state. Based on their latest post, you need to analyze their emotion and recommend an activity that could be beneficial for them. If the user is sad, suggest a comforting and soothing activity. If they are stressed, suggest a relaxing activity. If they are happy, encourage an activity that enhances their well-being. Be relevant, kind, and motivating in your response.");

            ObjectNode userMessageNode = objectMapper.createObjectNode();
            userMessageNode.put("role", "user");
            userMessageNode.put("content", "My latest post: " + userText);

            messagesNode.add(systemMessageNode);
            messagesNode.add(userMessageNode);

            rootNode.set("messages", messagesNode);
            rootNode.put("temperature", 0.7);
            rootNode.put("max_tokens", 150);
            rootNode.put("stream", false);

            // Conversion de l'objet Node en JSON String
            String llmRequestBody = objectMapper.writeValueAsString(rootNode);

            // Configuration de la requête HTTP
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.set("Content-Type", "application/json");

            HttpEntity<String> entity = new HttpEntity<>(llmRequestBody, headers);

            // Envoi de la requête au LLM
            ResponseEntity<String> response = restTemplate.exchange(LLM_API_URL, HttpMethod.POST, entity, String.class);

            // Parser la réponse pour extraire le texte généré
            JsonNode responseJson = objectMapper.readTree(response.getBody());
            JsonNode choices = responseJson.get("choices");

            if (choices != null && choices.isArray() && choices.size() > 0) {
                return choices.get(0).get("message").get("content").asText();
            } else {
                return "Aucune suggestion disponible.";
            }

        } catch (Exception e) {
            e.printStackTrace();
            return "Erreur lors de la génération de la suggestion.";
        }
    }

}
