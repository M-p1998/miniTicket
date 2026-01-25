package com.microservices.ticket.dto;

import com.microservices.ticket.model.TicketPriority;

public record TicketRequest(
        String subject,
        String description,
        TicketPriority priority,
        String createdBy
) {}