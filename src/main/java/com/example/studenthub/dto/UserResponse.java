package com.example.studenthub.dto;

import com.example.studenthub.entity.User;
import java.time.LocalDateTime;

public record UserResponse(Long userId, String username, String role, String status, LocalDateTime createdAt) {
    public static UserResponse from(User u) {
        return new UserResponse(u.getUserId(), u.getUsername(), u.getRole().getRoleName(), u.getStatus(), u.getCreatedAt());
    }
}