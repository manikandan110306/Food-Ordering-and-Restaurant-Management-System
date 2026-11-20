
package com.foodordering.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodordering.backend.entity.Notification;

public interface NotificationRepository
        extends JpaRepository<Notification, Long> {

    // Retrieve all notifications belonging to a user.
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);

    // Count unread notifications belonging to a user.
    long countByUserIdAndIsReadFalse(Long userId);

    // Find one notification belonging to a particular user.
    Optional<Notification> findByIdAndUserId(
            Long notificationId,
            Long userId
    );

    // Delete one notification belonging to a particular user.
    void deleteByIdAndUserId(
            Long notificationId,
            Long userId
    );

    // Delete all notifications belonging to a particular user.
    void deleteByUserId(Long userId);
}
