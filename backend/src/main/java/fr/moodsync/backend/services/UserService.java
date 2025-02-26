package fr.moodsync.backend.services;

import fr.moodsync.backend.models.User;
import fr.moodsync.backend.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // Ajouter un utilisateur
    public User createUser(User user) {
        user.setCreatedAt(Instant.now());
        user.setUpdatedAt(Instant.now());
        return userRepository.save(user);
    }

    // Mettre à jour un utilisateur
    public User updateUser(Integer id, User userDetails) {
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            throw new RuntimeException("User not found with id: " + id);
        }
        User existingUser = optionalUser.get();

        // Mettre à jour les champs
        existingUser.setEmail(userDetails.getEmail());
        existingUser.setPasswordHash(userDetails.getPasswordHash());
        existingUser.setName(userDetails.getName());
        existingUser.setRole(userDetails.getRole());
        existingUser.setPreferences(userDetails.getPreferences());
        existingUser.setProfilePictureUrl(userDetails.getProfilePictureUrl());
        existingUser.setConnectionType(userDetails.getConnectionType());
        existingUser.setExternalAccountId(userDetails.getExternalAccountId());
        existingUser.setUpdatedAt(Instant.now());

        return userRepository.save(existingUser);
    }

    // Supprimer un utilisateur par ID
    public void deleteUser(Integer id) {
        if (!userRepository.existsById(id)) {
            throw new RuntimeException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    // Trouver un utilisateur par ID
    public Optional<User> getUserById(Integer id) {
        return userRepository.findById(id);
    }

    // Lister tous les utilisateurs
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Trouver un utilisateur par email
    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
