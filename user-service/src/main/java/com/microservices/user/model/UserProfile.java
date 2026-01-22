package com.microservices.user.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(
    name = "user_profile",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_user_username", columnNames = "username"),
        @UniqueConstraint(name = "uk_user_email", columnNames = "email")
    }
)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // match Keycloak username later
    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String email;

    // free text title like "Analyst", "Support Engineer"
    @Column(nullable = false)
    private String title;

    // free text description / fun facts
    @Column(columnDefinition = "text")
    private String funFacts;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;
}
