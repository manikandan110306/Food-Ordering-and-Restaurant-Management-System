
package com.foodordering.backend.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodordering.backend.dto.NotificationResponse;

import com.foodordering.backend.entity.User;

import com.foodordering.backend.service.NotificationService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationService notificationService;

    // Get all notifications belonging to the logged-in user.

    @GetMapping
    public ResponseEntity<List<NotificationResponse>> getNotifications(
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(
                notificationService.getNotifications(user));
    }

    // Get the unread notification count.

    @GetMapping("/unread-count")
    public ResponseEntity<Long> getUnreadCount(
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(
                notificationService.getUnreadCount(user));
    }

    // Mark one notification as read.

    @PutMapping("/{notificationId}/read")
    public ResponseEntity<NotificationResponse> markAsRead(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal User user) {

        return ResponseEntity.ok(
                notificationService.markAsRead(notificationId, user));
    }

    // Mark all notifications as read.

    @PutMapping("/read-all")
    public ResponseEntity<String> markAllAsRead(
            @AuthenticationPrincipal User user) {

        notificationService.markAllAsRead(user);

        return ResponseEntity.ok(
                "All notifications marked as read");
    }

    // Delete one notification.

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<String> deleteNotification(
            @PathVariable Long notificationId,
            @AuthenticationPrincipal User user) {

        notificationService.deleteNotification(notificationId, user);

        return ResponseEntity.ok(
                "Notification deleted successfully");
    }

    // Delete all notifications.

    @DeleteMapping("/delete-all")
    public ResponseEntity<String> deleteAllNotifications(
            @AuthenticationPrincipal User user) {

        notificationService.deleteAllNotifications(user);

        return ResponseEntity.ok(
                "All notifications deleted successfully");
    }
}
