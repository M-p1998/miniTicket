package com.microservices.user.service;

import com.microservices.user.dto.UserRequest;
import com.microservices.user.dto.UserResponse;
import com.microservices.user.model.UserProfile;
import com.microservices.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;

    public UserResponse create(UserRequest req) {
        validateCreate(req);

        if (repo.existsByUsername(req.username().trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "username already exists");
        }
        if (repo.existsByEmail(req.email().trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email already exists");
        }

        UserProfile user = UserProfile.builder()
                .username(req.username().trim())
                .email(req.email().trim())
                .title(req.title().trim())
                .funFacts(req.funFacts())
                .createdAt(Instant.now())
                .build();

        return toResponse(repo.save(user));
    }

    public List<UserResponse> getAll() {
        return repo.findAll().stream().map(this::toResponse).toList();
    }

    public UserResponse getById(Long id) {
        UserProfile user = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found: " + id));
        return toResponse(user);
    }

    public UserResponse getByUsername(String username) {
        UserProfile user = repo.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found: " + username));
        return toResponse(user);
    }

    public UserResponse update(Long id, UserRequest req) {
        UserProfile user = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found: " + id));

        // username usually should not change (especially if tied to Keycloak)
        // We'll keep it immutable here. If you want it editable, tell me.

        if (req.email() != null && !req.email().isBlank() && !req.email().equals(user.getEmail())) {
            if (repo.existsByEmail(req.email().trim())) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "email already exists");
            }
            user.setEmail(req.email().trim());
        }

        if (req.title() != null && !req.title().isBlank()) {
            user.setTitle(req.title().trim());
        }

        // funFacts can be null (means clear it or keep it?)
        // We'll interpret null as "no change", and empty string as "set empty".
        if (req.funFacts() != null) {
            user.setFunFacts(req.funFacts());
        }

        return toResponse(repo.save(user));
    }

    public void delete(Long id) {
        if (!repo.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "user not found: " + id);
        }
        repo.deleteById(id);
    }

    private void validateCreate(UserRequest req) {
        if (req.username() == null || req.username().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username is required");
        }
        if (req.email() == null || req.email().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email is required");
        }
        if (req.title() == null || req.title().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "title is required");
        }
    }

    private UserResponse toResponse(UserProfile u) {
        return new UserResponse(
                u.getId(),
                u.getUsername(),
                u.getEmail(),
                u.getTitle(),
                u.getFunFacts(),
                u.getCreatedAt()
        );
    }
}
