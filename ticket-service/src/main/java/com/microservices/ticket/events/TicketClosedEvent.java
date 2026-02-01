package com.microservices.ticket.events;

public record TicketClosedEvent(Long ticketId, String closedBy, String closedAt) {}
