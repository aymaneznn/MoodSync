package fr.moodsync.backend.services;

import fr.moodsync.backend.models.User;
import fr.moodsync.backend.repository.UserRepository;
import fr.moodsync.backend.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final BCryptPasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public String login(String email, String password) {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPasswordHash())) {
                return jwtUtil.generateToken(user.getEmail());
            }
        }
        throw new RuntimeException("Email ou mot de passe incorrect !");
    }

    public String register(String email, String password, String name) {
        // Vérifie si l'utilisateur existe déjà
        if (userRepository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Cet email est déjà utilisé !");
        }

        // Hashage du mot de passe
        String hashedPassword = passwordEncoder.encode(password);

        // Création d'un nouvel utilisateur
        User newUser = new User();
        newUser.setEmail(email);
        newUser.setPasswordHash(hashedPassword);
        newUser.setName(name);
        newUser.setRole("user");  // Rôle par défaut

        // Sauvegarde en base
        userRepository.save(newUser);

        // Génération du token JWT
        return jwtUtil.generateToken(email);
    }
}
