package fr.moodsync.backend.controllers;

import fr.moodsync.backend.models.User;
import fr.moodsync.backend.repository.UserRepository;
import fr.moodsync.backend.services.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:5173", allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final AuthService authService;
    private final UserRepository userRepository;


    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Map<String, String> loginRequest) {
        String email = loginRequest.get("email");
        String password = loginRequest.get("password");

        // Authentification et génération du token
        String token = authService.login(email, password);

        // Récupère les détails de l'utilisateur (supposons que tu as une méthode pour ça)
        Optional<User> user = userRepository.findByEmail(email);

        // Crée une réponse contenant à la fois le token et l'utilisateur
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user);  // Renvoyer les informations de l'utilisateur

        return ResponseEntity.ok(response);
    }


    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> registerRequest) {
        String email = registerRequest.get("email");
        String password = registerRequest.get("password");
        String name = registerRequest.get("name");

        String token = authService.register(email, password, name);
        Optional<User> user = userRepository.findByEmail(email);

        // Crée une réponse contenant à la fois le token et l'utilisateur
        Map<String, Object> response = new HashMap<>();
        response.put("token", token);
        response.put("user", user);  // Renvoyer les informations de l'utilisateur

        return ResponseEntity.ok(response);
    }

}
