
package com.foodordering.backend.dto;

import java.time.LocalDateTime;

import com.foodordering.backend.entity.Notification;

import lombok.Getter;

@Getter
public class NotificationResponse {

    private Long id;

    private String title;

    private String message;

    private String type;

    private boolean read;

    private LocalDateTime createdAt;

    public NotificationResponse(Notification notification) {

        this.id = notification.getId();

        this.title = notification.getTitle();

        this.message = notification.getMessage();

        this.type = notification.getType().name();

        this.read = notification.isRead();

        this.createdAt = notification.getCreatedAt();
    }
}
