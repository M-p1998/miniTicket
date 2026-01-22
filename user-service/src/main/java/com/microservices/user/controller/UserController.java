package com.microservices.user.controller;

import com.microservices.user.dto.UserRequest;
import com.microservices.user.dto.UserResponse;
import com.microservices.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService service;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UserResponse create(@RequestBody UserRequest req) {
        return service.create(req);
    }

    @GetMapping
    public List<UserResponse> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public UserResponse getById(@PathVariable("id") Long id) {
        return service.getById(id);
    }

    @GetMapping("/by-username/{username}")
    public UserResponse getByUsername(@PathVariable String username) {
        return service.getByUsername(username);
    }

    @PutMapping("/{id}")
    public UserResponse update(@PathVariable("id") Long id, @RequestBody UserRequest req) {
        return service.update(id, req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
