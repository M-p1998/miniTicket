
package com.microservices.ticket.model;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ticket")
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class Ticket {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String subject;

    @Column(columnDefinition = "text")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TicketPriority priority;
    
    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private String createdBy;
    
    @PrePersist
    public void onCreate() {
        if (createdAt == null) createdAt = Instant.now();
    }
    
    @Column(nullable = true)
    private String closedBy;
    @Column(nullable = true)
    private Instant closedAt;

}
