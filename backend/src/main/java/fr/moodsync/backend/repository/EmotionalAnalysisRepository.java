package fr.moodsync.backend.repository;

import fr.moodsync.backend.models.EmotionalAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmotionalAnalysisRepository extends JpaRepository<EmotionalAnalysis, Integer> {
}