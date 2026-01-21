package com.microservices.ticket.service;
import java.util.List;

import org.springframework.stereotype.Service;

import com.microservices.ticket.dto.TicketRequest;
import com.microservices.ticket.dto.TicketResponse;
import com.microservices.ticket.model.Ticket;
import com.microservices.ticket.model.TicketStatus;
import com.microservices.ticket.repository.TicketRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {
	
	private final TicketRepository ticketRepository;
	
	public TicketResponse createTicket(TicketRequest ticketRequest) {
		Ticket ticket = Ticket.builder()
				.subject(ticketRequest.subject())
				.description(ticketRequest.description())
				.status(TicketStatus.OPEN) //default
				.priority(ticketRequest.priority())
				.build();
		 	Ticket saved = ticketRepository.save(ticket);

	        log.info("Ticket created successfully: id={}", saved.getId());

	        return toResponse(saved);
	}

	public List<TicketResponse> getAllTickets() {
		
		return ticketRepository.findAll()
				.stream()
				.map(ticket -> new TicketResponse(ticket.getId(), ticket.getSubject(), ticket.getDescription(), ticket.getStatus(), ticket.getPriority()))
				.toList();
	}
	public TicketResponse getTicket(Long id) {
        Ticket ticket = ticketRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Ticket not found: " + id));
        return toResponse(ticket);
    }
	
	private TicketResponse toResponse(Ticket t) {
        return new TicketResponse(t.getId(), t.getSubject(), t.getDescription(), t.getStatus(), t.getPriority());
    }
}
