
package com.foodordering.backend.controller;

import java.util.List;
import java.util.Map;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodordering.backend.dto.MenuItemRequest;
import com.foodordering.backend.dto.MenuItemResponse;
import com.foodordering.backend.entity.MenuItem;
import com.foodordering.backend.entity.Role;
import com.foodordering.backend.entity.User;
import com.foodordering.backend.service.MenuItemService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/menu-items")
@RequiredArgsConstructor
public class MenuItemController {

    private final MenuItemService menuItemService;

    // Add a new food item
    @PostMapping("/restaurant/{restaurantId}")
    public ResponseEntity<?> addMenuItem(
            @PathVariable Long restaurantId,
            @Valid @RequestBody MenuItemRequest request,
            Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getRole() != Role.RESTAURANT_OWNER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message",
                            "Only restaurant owners can add menu items"));
        }

        try {
            MenuItem menuItem = menuItemService.addMenuItem(
                    restaurantId, request, currentUser);

            MenuItemResponse response = new MenuItemResponse(menuItem);

            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(response);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    // Get all menu items belonging to a restaurant
    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<?> getMenuItems(
            @PathVariable Long restaurantId,
            Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getRole() != Role.RESTAURANT_OWNER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message",
                            "Only restaurant owners can view this menu"));
        }

        try {
            List<MenuItem> menuItems =
                    menuItemService.getMenuItemsByRestaurant(
                            restaurantId, currentUser);

            List<MenuItemResponse> response = menuItems.stream()
                    .map(MenuItemResponse::new)
                    .toList();

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    // Update an existing food item
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMenuItem(
            @PathVariable Long id,
            @Valid @RequestBody MenuItemRequest request,
            Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getRole() != Role.RESTAURANT_OWNER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message",
                            "Only restaurant owners can update menu items"));
        }

        try {
            MenuItem menuItem = menuItemService.updateMenuItem(
                    id, request, currentUser);

            MenuItemResponse response = new MenuItemResponse(menuItem);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    // Delete a food item
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMenuItem(
            @PathVariable Long id,
            Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getRole() != Role.RESTAURANT_OWNER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message",
                            "Only restaurant owners can delete menu items"));
        }

        try {
            menuItemService.deleteMenuItem(id, currentUser);

            return ResponseEntity.ok(
                    Map.of("message", "Menu item deleted successfully"));

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }

    // Change food item availability
    @PatchMapping("/{id}/availability")
    public ResponseEntity<?> updateAvailability(
            @PathVariable Long id,
            @RequestParam boolean available,
            Authentication authentication) {

        User currentUser = (User) authentication.getPrincipal();

        if (currentUser.getRole() != Role.RESTAURANT_OWNER) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body(Map.of("message",
                            "Only restaurant owners can change availability"));
        }

        try {
            MenuItem menuItem = menuItemService.updateAvailability(
                    id, available, currentUser);

            MenuItemResponse response = new MenuItemResponse(menuItem);

            return ResponseEntity.ok(response);

        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest()
                    .body(Map.of("message", ex.getMessage()));
        }
    }
}
