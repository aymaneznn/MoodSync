package fr.moodsync.backend.repositories;

import fr.moodsync.backend.models.SearchHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SearchHistoryRepository extends JpaRepository<SearchHistory, Integer> {
}