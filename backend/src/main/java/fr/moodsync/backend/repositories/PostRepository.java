package fr.moodsync.backend.repositories;

import fr.moodsync.backend.models.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Integer> {
}