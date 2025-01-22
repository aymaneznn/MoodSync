package fr.moodsync.backend.repositories;

import fr.moodsync.backend.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
}