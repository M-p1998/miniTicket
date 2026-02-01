package com.microservices.comment.kafka;

public record TicketClosedEvent(Long ticketId, String closedBy, String closedAt) {}

