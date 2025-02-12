package fr.moodsync.backend.repositories;

import fr.moodsync.backend.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Integer> {
    List<Post> findByUserId(Integer userId);
}