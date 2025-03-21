package fr.moodsync.backend.services;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.client.HttpClientErrorException;

@Service
public class MistralService {

    private final RestTemplate restTemplate;

    public MistralService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getResponseFromMistral(String inputText) {
        try {
            // URL du service Python Flask
            String url = "http://localhost:5000/generate";
            
            // Préparer la requête JSON à envoyer
            String requestBody = "{\"text\": \"" + inputText + "\"}";
            
            // Faire une requête POST vers l'API Python
            ResponseEntity<String> response = restTemplate.postForEntity(url, requestBody, String.class);
            
            // Extraire la réponse du modèle (s'il y en a une)
            String responseBody = response.getBody();
            
            // Retourner la réponse obtenue
            return responseBody;
        } catch (HttpClientErrorException e) {
            e.printStackTrace();
            return "Error calling Mistral service: " + e.getMessage();
        }
    }
}
