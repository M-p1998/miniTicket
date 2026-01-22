package com.microservices.user.dto;

import java.time.Instant;

public record UserResponse(
        Long id,
        String username,
        String email,
        String title,
        String funFacts,
        Instant createdAt
) {}
