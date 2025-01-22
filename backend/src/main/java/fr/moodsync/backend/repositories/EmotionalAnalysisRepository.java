package fr.moodsync.backend.repositories;

import fr.moodsync.backend.models.EmotionalAnalysis;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmotionalAnalysisRepository extends JpaRepository<EmotionalAnalysis, Integer> {
}