package com.microservices.comment.dto;

import java.time.Instant;

public record CommentResponse(
        Long id,
        Long ticketId,
        String author,
        String message,
        Instant createdAt
) {}
