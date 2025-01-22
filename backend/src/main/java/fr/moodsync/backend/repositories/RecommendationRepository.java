package fr.moodsync.backend.repositories;

import fr.moodsync.backend.models.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RecommendationRepository extends JpaRepository<Recommendation, Integer> {
}