package com.microservices.ticket.dto;

import java.time.Instant;

import com.microservices.ticket.model.TicketPriority;
import com.microservices.ticket.model.TicketStatus;

public record TicketResponse(
		Long id,
        String subject,
        String description,
        TicketStatus status,
        TicketPriority priority,
        String createdBy,
        Instant createdAt
        )
{}
