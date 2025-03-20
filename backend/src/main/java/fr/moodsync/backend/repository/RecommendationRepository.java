package fr.moodsync.backend.repository;

import fr.moodsync.backend.models.Recommendation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecommendationRepository extends JpaRepository<Recommendation, Integer> {
    List<Recommendation> findByUserId(int userId);
}