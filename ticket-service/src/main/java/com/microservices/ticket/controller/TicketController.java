package com.microservices.ticket.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.ticket.dto.TicketRequest;
import com.microservices.ticket.dto.TicketResponse;
import com.microservices.ticket.model.TicketStatus;
import com.microservices.ticket.service.TicketService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/tickets")
@RequiredArgsConstructor
public class TicketController {
	
	private final TicketService ticketService;

//	@PostMapping
//	@ResponseStatus(HttpStatus.CREATED)
//	public TicketResponse createTicket(@RequestBody TicketRequest ticketRequest) {
//		return ticketService.createTicket(ticketRequest);
//		
//	}
	
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public TicketResponse createTicket(
	        @RequestBody TicketRequest ticketRequest,
	        @AuthenticationPrincipal Jwt jwt
	) {
	    String createdBy = jwt.getClaimAsString("display_username");
	    if (createdBy == null) {
	        createdBy = jwt.getSubject(); // fallback
	    }

	    return ticketService.createTicket(ticketRequest, createdBy);
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
	                                   @RequestParam("status") TicketStatus status,@AuthenticationPrincipal Jwt jwt) {
		String user = jwt.getClaimAsString("display_username");
		return ticketService.updateStatus(id, status, user);
	}
	
	@DeleteMapping("/{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable("id") Long id) {
	    ticketService.deleteTicket(id);
	}
	
	

}
