package fr.moodsync.backend.repositories;

import fr.moodsync.backend.models.ContentMetadatum;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ContentMetadatumRepository extends JpaRepository<ContentMetadatum, Integer> {
}