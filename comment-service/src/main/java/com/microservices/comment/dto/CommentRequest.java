package com.microservices.comment.dto;


public record CommentRequest(
        Long ticketId,
        String author,
        String message
) {}
