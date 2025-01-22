package fr.moodsync.backend.repositories;

import fr.moodsync.backend.models.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Integer> {
}