package com.microservices.ticket.controller;

import com.microservices.ticket.dto.TicketRequest;
import com.microservices.ticket.dto.TicketResponse;
import com.microservices.ticket.model.TicketStatus;
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
    public TicketResponse getTicket(@PathVariable("id") Long id) {
        return ticketService.getTicket(id);
    }
	
	@PutMapping("/{id}")
	public TicketResponse update(@PathVariable("id") Long id,
	                             @RequestBody TicketRequest req) {
	    return ticketService.updateTicket(id, req);
	}
	
	@PatchMapping("/{id}/status")
	public TicketResponse updateStatus(@PathVariable("id") Long id,
	                                   @RequestParam("status") TicketStatus status) {
	    return ticketService.updateStatus(id, status);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) {
	    ticketService.deleteTicket(id);
	}
	
	

}
