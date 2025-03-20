package fr.moodsync.backend.controllers;

import fr.moodsync.backend.models.Recommendation;
import fr.moodsync.backend.services.RecommendationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
public class RecommendationController {
    private final RecommendationService recommendationService;

    public RecommendationController(RecommendationService recommendationService) {
        this.recommendationService = recommendationService;
    }

    @PostMapping("/add")
    public ResponseEntity<Recommendation> addRecommendation(@RequestBody Recommendation recommendation) {
        Recommendation savedRecommendation = recommendationService.addRecommendation(recommendation);
        return ResponseEntity.ok(savedRecommendation);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteRecommendation(@PathVariable Integer id) {
        recommendationService.deleteRecommendation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Recommendation> getRecommendationById(@PathVariable Integer id) {
        return ResponseEntity.ok(recommendationService.getRecommendationById(id));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Recommendation>> getRecommendationsByUser(@PathVariable Integer userId) {
        return ResponseEntity.ok(recommendationService.getRecommendationsByUser(userId));
    }
    
}
