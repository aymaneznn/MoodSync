package fr.moodsync.backend.services;

import fr.moodsync.backend.models.Recommendation;
import fr.moodsync.backend.repository.RecommendationRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class RecommendationService {
    private final RecommendationRepository recommendationRepository;

    public RecommendationService(RecommendationRepository recommendationRepository) {
        this.recommendationRepository = recommendationRepository;
    }

    public Recommendation addRecommendation(Recommendation recommendation) {
        return recommendationRepository.save(recommendation);
    }

    public void deleteRecommendation(Integer id) {
        recommendationRepository.deleteById(id);
    }

    public Recommendation getRecommendationById(Integer id) {
        return recommendationRepository.findById(id).orElseThrow(() -> new RuntimeException("Recommendation not found"));
    }

    public List<Recommendation> getRecommendationsByUser(Integer userId) {
        return recommendationRepository.findByUserId(userId);
    }
}
