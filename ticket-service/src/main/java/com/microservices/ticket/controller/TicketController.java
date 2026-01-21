package com.microservices.ticket.controller;

import com.microservices.ticket.dto.TicketRequest;
import com.microservices.ticket.dto.TicketResponse;
import com.microservices.ticket.service.TicketService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
	
	private final TicketService ticketService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public TicketResponse createTicket(@RequestBody TicketRequest ticketRequest) {
		return ticketService.createTicket(ticketRequest);
		
	}
	
	@GetMapping
	@ResponseStatus(HttpStatus.OK)
	public List<TicketResponse> getAllTickets(){
		return ticketService.getAllTickets();
	}
	
	@GetMapping("/{id}")
    public TicketResponse getTicket(@PathVariable Long id) {
        return ticketService.getTicket(id);
    }
}
