
package com.foodordering.backend.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.foodordering.backend.dto.NotificationResponse;

import com.foodordering.backend.entity.Notification;
import com.foodordering.backend.entity.NotificationType;
import com.foodordering.backend.entity.User;

import com.foodordering.backend.repository.NotificationRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;

    // Create a notification for a user.

    @Transactional
    public void createNotification(
            User user,
            String title,
            String message,
            NotificationType type) {

        Notification notification = new Notification();

        notification.setUser(user);

        notification.setTitle(title);

        notification.setMessage(message);

        notification.setType(type);

        notification.setRead(false);

        notificationRepository.save(notification);
    }

    // Retrieve all notifications belonging to a user.

    @Transactional(readOnly = true)
    public List<NotificationResponse> getNotifications(User user) {

        return notificationRepository
                .findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(NotificationResponse::new)
                .toList();
    }

    // Count unread notifications.

    @Transactional(readOnly = true)
    public long getUnreadCount(User user) {

        return notificationRepository
                .countByUserIdAndIsReadFalse(user.getId());
    }

    // Mark one notification as read.

    @Transactional
    public NotificationResponse markAsRead(
            Long notificationId,
            User user) {

        Notification notification = notificationRepository
                .findByIdAndUserId(notificationId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Notification not found"));

        notification.setRead(true);

        Notification updatedNotification =
                notificationRepository.save(notification);

        return new NotificationResponse(updatedNotification);
    }

    // Mark all notifications as read.

    @Transactional
    public void markAllAsRead(User user) {

        List<Notification> notifications = notificationRepository
                .findByUserIdOrderByCreatedAtDesc(user.getId());

        for (Notification notification : notifications) {

            notification.setRead(true);
        }

        notificationRepository.saveAll(notifications);
    }

    // Delete one notification.

    @Transactional
    public void deleteNotification(
            Long notificationId,
            User user) {

        Notification notification = notificationRepository
                .findByIdAndUserId(notificationId, user.getId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Notification not found"));

        notificationRepository.delete(notification);
    }

    // Delete all notifications belonging to a user.

    @Transactional
    public void deleteAllNotifications(User user) {

        notificationRepository.deleteByUserId(user.getId());
    }
}
