package com.microservices.user.dto;

public record UserRequest(
        String username,
        String email,
        String title,
        String funFacts
) {}
