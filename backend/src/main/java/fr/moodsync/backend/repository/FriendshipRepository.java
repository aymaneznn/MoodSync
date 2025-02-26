package fr.moodsync.backend.repository;

import fr.moodsync.backend.models.Friendship;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FriendshipRepository extends JpaRepository<Friendship, Integer> {
}