package com.microservices.user.service;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.microservices.user.dto.UserRequest;
import com.microservices.user.dto.UserResponse;
import com.microservices.user.model.UserProfile;
import com.microservices.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository repo;
//    public UserService(UserRepository repo) {
//        this.repo = repo;
//    }

    public UserResponse create(UserRequest req, String keycloakUserId, String usernameFromToken,
            String emailFromToken) {
        validateCreate(req);
        
        if (keycloakUserId == null || keycloakUserId.isBlank()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "missing keycloak user id");
        }

        // prevent creating 2 profiles for same Keycloak user
        if (repo.existsByKeycloakUserId(keycloakUserId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "profile already exists for this user");
        }

        if (repo.existsByUsername(req.username().trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "username already exists");
        }
        if (repo.existsByEmail(req.email().trim())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "email already exists");
        }
//        if (req.title() == null || req.title().isBlank())
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "title is required");

        UserProfile user = UserProfile.builder()
                .username(req.username().trim())
                .email(req.email().trim())
                .title(
                        (req.title() == null || req.title().isBlank())
                            ? ""
                            : req.title().trim()
                    )
                .funFacts(req.funFacts())
                .keycloakUserId(keycloakUserId)
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
        if (req.username() != null && !req.username().isBlank()
                && !req.username().equals(user.getUsername())) {

            if (repo.existsByUsername(req.username().trim())) {
                throw new ResponseStatusException(
                        HttpStatus.CONFLICT, "username already exists");
            }

            user.setUsername(req.username().trim());
        }

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
    
    public UserResponse getByKeycloakUserId(String keycloakUserId) {
        UserProfile user = repo.findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "profile not found"));
        return toResponse(user);
    }
    
//    public UserResponse getByKeycloakUserId(String keycloakUserId, String username, String email) {
//
//        return repo.findByKeycloakUserId(keycloakUserId)
//            .or(() -> repo.findByUsername(username))
//            .map(this::toResponse)
//            .orElseThrow(() ->
//                new ResponseStatusException(HttpStatus.NOT_FOUND, "profile not found")
//            );
//    }



    private void validateCreate(UserRequest req) {
        if (req.username() == null || req.username().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "username is required");
        }
        if (req.email() == null || req.email().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "email is required");
        }
//        if (req.title() == null || req.title().isBlank()) {
//            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "title is required");
//        }
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
