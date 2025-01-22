package fr.moodsync.backend.repositories;

import fr.moodsync.backend.models.SessionHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionHistoryRepository extends JpaRepository<SessionHistory, Integer> {
}