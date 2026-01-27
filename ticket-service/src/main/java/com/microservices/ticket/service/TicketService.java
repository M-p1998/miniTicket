package com.microservices.ticket.service;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.microservices.ticket.dto.TicketRequest;
import com.microservices.ticket.dto.TicketResponse;
import com.microservices.ticket.model.Ticket;
import com.microservices.ticket.model.TicketStatus;
import com.microservices.ticket.repository.TicketRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class TicketService {
	
	private final TicketRepository ticketRepository;
	
	public TicketResponse createTicket(TicketRequest ticketRequest, String createdBy) {
		Ticket ticket = Ticket.builder()
				.subject(ticketRequest.subject())
				.description(ticketRequest.description())
				.status(TicketStatus.OPEN) //default
				.priority(ticketRequest.priority())
				.createdBy(createdBy)
				.build();
		 	Ticket saved = ticketRepository.save(ticket);

	        log.info("Ticket created successfully: id={}", saved.getId());

	        return toResponse(saved);
	}

	public List<TicketResponse> getAllTickets() {
		
		return ticketRepository.findAll()
				.stream()
				.map(ticket -> new TicketResponse(ticket.getId(), ticket.getSubject(), ticket.getDescription(), ticket.getStatus(), ticket.getPriority(), ticket.getCreatedBy(),
					ticket.getCreatedAt(),ticket.getClosedBy(),
			        ticket.getClosedAt()))
				.toList();
	}
	public TicketResponse getTicket(Long id) {
	    Ticket ticket = ticketRepository.findById(id)
	        .orElseThrow(() ->
	            new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found: " + id)
	        );
	    return toResponse(ticket);
	}
	
	private TicketResponse toResponse(Ticket t) {
	    return new TicketResponse(
	        t.getId(),
	        t.getSubject(),
	        t.getDescription(),
	        t.getStatus(),
	        t.getPriority(),
	        t.getCreatedBy(),
	        t.getCreatedAt(),
	        t.getClosedBy(),
	        t.getClosedAt()
	    );
	}
	
	public TicketResponse updateTicket(Long id, TicketRequest req) {
	    Ticket ticket = ticketRepository.findById(id)
	        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found: " + id));

	    ticket.setSubject(req.subject());
	    ticket.setDescription(req.description());
	    ticket.setPriority(req.priority());

	    Ticket saved = ticketRepository.save(ticket);
	    return toResponse(saved);
	}
	
	public TicketResponse updateStatus(Long id, TicketStatus status, String user) {
	    Ticket ticket = ticketRepository.findById(id)
	        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found: " + id));

	    ticket.setStatus(status);
	    if (status == TicketStatus.CLOSED) {
	        ticket.setClosedBy(user);
	        ticket.setClosedAt(Instant.now());
	      }
	    Ticket saved = ticketRepository.save(ticket);
	    return toResponse(saved);
	}
	
	public void deleteTicket(Long id) {
	    if (!ticketRepository.existsById(id)) {
	    	throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Ticket not found: " + id);
	    }
	    ticketRepository.deleteById(id);
	}


	
	

}



