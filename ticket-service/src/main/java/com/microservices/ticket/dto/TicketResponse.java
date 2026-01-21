package com.microservices.ticket.dto;

import com.microservices.ticket.model.TicketPriority;
import com.microservices.ticket.model.TicketStatus;

public record TicketResponse(
		Long id,
        String subject,
        String description,
        TicketStatus status,
        TicketPriority priority
        )
{}
