package com.locallink.controller;

import com.locallink.dto.request.LoginRequest;
import com.locallink.dto.request.RegisterRequest;
import com.locallink.dto.request.UpdateProfileRequest;
import com.locallink.dto.response.LoginResponse;
import com.locallink.dto.response.ProfileResponse;
import com.locallink.dto.response.UserRegistrationResponse;
import com.locallink.service.impl.UserServiceImpl;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
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
    public ProfileResponse profile(Authentication authentication) {

        String email = authentication.getName();

        return userService.getProfile(email);
    }

    @PutMapping("/profile")
    public ProfileResponse updateProfile(
            Authentication authentication,
            @Valid @RequestBody UpdateProfileRequest request) {

        String email = authentication.getName();

        return userService.updateProfile(email, request);
    }

    @GetMapping("/customer-test")
    public String customerTest() {
        return "Customer access granted.";
    }
}
