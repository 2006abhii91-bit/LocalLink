package com.locallink.controller;

import com.locallink.dto.RegisterRequest;
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
}
