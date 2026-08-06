package com.locallink.controller;

import com.locallink.dto.request.LoginRequest;
import com.locallink.dto.request.RegisterRequest;
import com.locallink.dto.response.LoginResponse;
import com.locallink.dto.response.UserRegistrationResponse;
import com.locallink.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserServiceImpl userService;

    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public UserRegistrationResponse registerUser(@Valid @RequestBody RegisterRequest request) {
        return userService.registerUser(request);
    }

    @PostMapping("/login")
    public LoginResponse loginUser(@Valid @RequestBody LoginRequest request) {
        return userService.loginUser(request);
    }

    @GetMapping("/profile")
    public String profile() {
        return "Welcome! You are authenticated.";
    }
}
