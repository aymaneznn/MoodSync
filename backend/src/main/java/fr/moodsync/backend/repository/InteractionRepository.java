package fr.moodsync.backend.repository;

import fr.moodsync.backend.models.Interaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InteractionRepository extends JpaRepository<Interaction, Integer> {
}