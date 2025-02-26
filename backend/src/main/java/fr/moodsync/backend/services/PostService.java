package fr.moodsync.backend.services;

import fr.moodsync.backend.models.Post;
import fr.moodsync.backend.models.User;
import fr.moodsync.backend.repository.PostRepository;
import fr.moodsync.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Duration;
import java.time.Instant;
import java.util.List;

@Service
@Transactional
public class PostService {
    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostService(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    // 🔹 Récupérer tous les posts
    public List<Post> getAllPosts() {
        List<Post> posts = postRepository.findAll();
        if (posts.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun post trouvé.");
        }
        return posts;
    }

    // 🔹 Récupérer un post par ID
    public Post getPostById(Integer id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post non trouvé avec l'ID : " + id));
    }

    // 🔹 Récupérer les posts d’un utilisateur
    public List<Post> getPostsByUserId(Integer userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé avec l'ID : " + userId);
        }

        List<Post> posts = postRepository.findByUserId(userId);
        if (posts.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Aucun post trouvé pour l'utilisateur ID : " + userId);
        }

        return posts;
    }

    // 🔹 Créer un nouveau post
    public Post createPost(Integer userId, Post post) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé avec l'ID : " + userId));

        // Validation des données
        if (post.getContent() == null || post.getContent().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le contenu du post ne peut pas être vide.");
        }

        post.setUser(user);
        post.setCreatedAt(Instant.now().plus(Duration.ofHours(1)));
        post.setUpdatedAt(Instant.now());
        post.setLikesCount(0); // Initialise le nombre de likes à 0

        return postRepository.save(post);
    }

    // 🔹 Mettre à jour un post
    public Post updatePost(Integer id, Post postDetails) {
        Post existingPost = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Post non trouvé avec l'ID : " + id));

        // Vérifier si l'utilisateur est bien le propriétaire du post
        if (postDetails.getUser() == null || !existingPost.getUser().getId().equals(postDetails.getUser().getId())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Vous ne pouvez pas modifier ce post.");
        }

        // Validation des données
        if (postDetails.getContent() == null || postDetails.getContent().trim().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Le contenu du post ne peut pas être vide.");
        }

        // Mise à jour des données
        existingPost.setContent(postDetails.getContent());
        existingPost.setMediaUrl(postDetails.getMediaUrl());
        existingPost.setVisibility(postDetails.getVisibility());
        existingPost.setTags(postDetails.getTags());
        existingPost.setUpdatedAt(Instant.now());

        return postRepository.save(existingPost);
    }

    // 🔹 Supprimer un post
    public void deletePost(Integer id) {
        if (!postRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Post non trouvé avec l'ID : " + id);
        }
        postRepository.deleteById(id);
    }
}
