package com.microservices.ticket.dto;

import java.time.Instant;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.microservices.ticket.model.TicketPriority;
import com.microservices.ticket.model.TicketStatus;

//@JsonTypeInfo(use = JsonTypeInfo.Id.CLASS, property = "@class")
public record TicketResponse(
		Long id,
        String subject,
        String description,
        TicketStatus status,
        TicketPriority priority,
        String createdBy,
        Instant createdAt,
        
        String closedBy,
        Instant closedAt
        )
{}
