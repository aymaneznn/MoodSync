package fr.moodsync.backend.controllers;

import fr.moodsync.backend.models.Post;
import fr.moodsync.backend.services.PostService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    // 🔹 Récupérer tous les posts
    @GetMapping
    public List<Post> getAllPosts() {
        return postService.getAllPosts();
    }

    // 🔹 Récupérer un post par ID
    @GetMapping("/{id}")
    public ResponseEntity<Post> getPostById(@PathVariable Integer id) {
        Optional<Post> post = Optional.ofNullable(postService.getPostById(id));
        return post.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // 🔹 Récupérer tous les posts d'un utilisateur
    @GetMapping("/user/{userId}")
    public List<Post> getPostsByUserId(@PathVariable Integer userId) {
        return postService.getPostsByUserId(userId);
    }

    // 🔹 Créer un post
    @PostMapping("/user/{userId}")
    public ResponseEntity<Post> createPost(@PathVariable Integer userId, @RequestBody Post post) {
        System.out.println("Post: " + post);
        Post newPost = postService.createPost(userId, post);
        return ResponseEntity.ok(newPost);
    }

    // 🔹 Mettre à jour un post
    @PutMapping("/{id}")
    public ResponseEntity<Post> updatePost(@PathVariable Integer id, @RequestBody Post postDetails) {
        return ResponseEntity.ok(postService.updatePost(id, postDetails));
    }

    // 🔹 Supprimer un post
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePost(@PathVariable Integer id) {
        postService.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    // 🔹 Liker un post
    @PostMapping("/like-post/{postId}")
    public ResponseEntity<Void> likePost(@PathVariable Integer postId) {
        postService.likePost(postId);
        return ResponseEntity.noContent().build();
    }

    // 🔹 Unliker un post
    @PostMapping("/unlike-post/{postId}")
    public ResponseEntity<Void> unlikePost(@PathVariable Integer postId) {
        postService.unlikePost(postId);
        return ResponseEntity.noContent().build();
    }

    // Add tags to a post
    @PostMapping("/add-tags/{postId}")
    public ResponseEntity<Void> addComment(@PathVariable Integer postId, @RequestBody List<Map<String, Object>> tags) {
        postService.addComment(postId, tags);
        return ResponseEntity.noContent().build();
    }

    // get tags of a post
    @GetMapping("/comments/{postId}")
    public List<Map<String, Object>> getTags(@PathVariable Integer postId) {
        return postService.getTags(postId);
    }

    //put post
    @PutMapping("/put-post/{id}")
    public ResponseEntity<Post> putPost(@PathVariable Integer id, @RequestBody Post post) {
        return ResponseEntity.ok(postService.putPost(id, post));
    }
}
