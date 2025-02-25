package fr.moodsync.backend.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.moodsync.backend.models.EmotionResponse;
import fr.moodsync.backend.models.Movie;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmotionService {

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
            String suggestion = jsonNode.has("suggestion") ? jsonNode.get("suggestion").asText() : "No suggestion available";

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
}
