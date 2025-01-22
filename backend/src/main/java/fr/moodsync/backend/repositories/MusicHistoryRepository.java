package fr.moodsync.backend.repositories;

import fr.moodsync.backend.models.MusicHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicHistoryRepository extends JpaRepository<MusicHistory, Integer> {
}