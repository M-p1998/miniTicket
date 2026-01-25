package com.microservices.user.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.microservices.user.dto.UserRequest;
import com.microservices.user.dto.UserResponse;
import com.microservices.user.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

//    @PostMapping
//    @ResponseStatus(HttpStatus.CREATED)
//    public UserResponse create(@RequestBody UserRequest req) {
//        return service.create(req);
//    }
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(
            @RequestBody UserRequest req,
            @AuthenticationPrincipal Jwt jwt
    ) {
        String keycloakUserId = jwt.getSubject(); 
        String username = jwt.getClaimAsString("preferred_username");
        String email = jwt.getClaimAsString("email");
        
        
        return service.create(req, keycloakUserId, username, email);
    }



    @GetMapping
    public List<UserResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id:\\d+}")
    public UserResponse getById(@PathVariable("id") Long id) {
        return service.getById(id);
    }

//    @GetMapping("/by-username/{username}")
//    public UserResponse getByUsername(@PathVariable String username) {
//        return service.getByUsername(username);
//    }
    @GetMapping("/me")
    public UserResponse me(@AuthenticationPrincipal Jwt jwt) {
    	String keycloakUserId = jwt.getSubject();
        return service.getByKeycloakUserId(keycloakUserId);
    }
    
//    @GetMapping("/me")
//    public UserResponse me(@AuthenticationPrincipal Jwt jwt) {
//        return service.getByKeycloakUserId(
//            jwt.getSubject(),
//            jwt.getClaimAsString("preferred_username"),
//            jwt.getClaimAsString("email")
//        );
//    }
    
//    @GetMapping("/me")
//    public UserResponse me(@AuthenticationPrincipal Jwt jwt) {
//        if (jwt == null) {
//            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing JWT");
//        }
//        return service.getByKeycloakUserId(jwt.getSubject(),
//        	  jwt.getClaimAsString("preferred_username"),
//              jwt.getClaimAsString("email"));
//    }



    @PutMapping("/{id:\\d+}")
    public UserResponse update(@PathVariable("id") Long id, @RequestBody UserRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id:\\d+}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
    
    
    
}
