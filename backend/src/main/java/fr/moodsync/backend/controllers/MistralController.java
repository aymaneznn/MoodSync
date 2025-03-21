package fr.moodsync.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;

import java.util.Map;

@RestController
@RequestMapping("/api/llm")
public class MistralController {

    private final String LLM_API_URL = "http://localhost:1234/v1/chat/completions"; // Remplace par ton URL

    @PostMapping("/generate-lyrics")
    public ResponseEntity<String> generateLyrics(@RequestBody Map<String, String> request) {
        String userPost = request.get("text"); // Récupère le post de l'utilisateur

        // Création du JSON pour l'API LLM
        String llmRequestBody = "{"
                + "\"model\": \"mathstral-7b-v0.1\","
                + "\"messages\": ["
                + "    { \"role\": \"system\", \"content\": \"You are a creative AI that writes song lyrics based on the user's emotions. You always generate lyrics that match their mood and help enhance it. If they are sad, write uplifting lyrics to cheer them up. If they are happy, write lyrics that amplify their joy. If they are nostalgic, write something that resonates with their memories. Use poetic and musical language, and specify a fitting music genre.\" },"
                + "    { \"role\": \"user\", \"content\": \"" + userPost + "\" }"
                + "],"
                + "\"temperature\": 0.9,"
                + "\"max_tokens\": 300,"
                + "\"stream\": false"
                + "}";

        // Configuration de la requête HTTP
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/json");

        HttpEntity<String> entity = new HttpEntity<>(llmRequestBody, headers);

        // Envoi de la requête au LLM
        ResponseEntity<String> response = restTemplate.exchange(LLM_API_URL, HttpMethod.POST, entity, String.class);

        return ResponseEntity.ok(response.getBody());
    }
}
