package com.microservices.comment.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.microservices.comment.dto.CommentRequest;
import com.microservices.comment.dto.CommentResponse;
import com.microservices.comment.service.CommentService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/comments")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService service;

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public CommentResponse create(@RequestBody CommentRequest req) {
//        return service.create(req);
//    }
    
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CommentResponse create(@RequestBody CommentRequest req,
            @AuthenticationPrincipal Jwt jwt) {
    		
    	if (jwt == null) {
            throw new ResponseStatusException(
                    HttpStatus.UNAUTHORIZED,
                    "Missing JWT"
            );
        }

		String author = jwt.getClaimAsString("display_username");
		
		// create a new request that includes the author
		CommentRequest withAuthor = new CommentRequest(
		req.ticketId(),
		author,
		req.message()
	);
		
	return service.create(withAuthor);
	}

    @GetMapping
    public List<CommentResponse> getByTicket(@RequestParam("ticketId") Long ticketId) {
        return service.getByTicket(ticketId);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
