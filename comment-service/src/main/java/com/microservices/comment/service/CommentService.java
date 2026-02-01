package com.microservices.comment.service;

import com.microservices.comment.dto.*;
import com.microservices.comment.model.Comment;
import com.microservices.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {

    private final CommentRepository repo;

    public CommentResponse create(CommentRequest req) {
        Comment comment = Comment.builder()
                .ticketId(req.ticketId())
                .author(req.author())
                .message(req.message())
                .createdAt(Instant.now())
                .build();

        return toResponse(repo.save(comment));
    }

    public List<CommentResponse> getByTicket(Long ticketId) {
        return repo.findByTicketId(ticketId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public void delete(Long id) {
        if (!repo.existsById(id))
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Comment not found");
        repo.deleteById(id);
    }

    private CommentResponse toResponse(Comment c) {
        return new CommentResponse(
                c.getId(), c.getTicketId(), c.getAuthor(), c.getMessage(), c.getCreatedAt()
        );
    }
    
    public CommentResponse createSystemComment(Long ticketId, String message) {
    	  Comment c = new Comment();
    	  c.setTicketId(ticketId);
    	  c.setAuthor("system");
    	  c.setMessage(message);
    	  c.setCreatedAt(Instant.now());
    	  return toResponse(repo.save(c));
    	}

}
