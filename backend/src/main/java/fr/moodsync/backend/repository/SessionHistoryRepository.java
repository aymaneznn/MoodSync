package fr.moodsync.backend.repository;

import fr.moodsync.backend.models.SessionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionHistoryRepository extends JpaRepository<SessionHistory, Integer> {
}