package com.locallink.service.impl;

import com.locallink.dto.request.LoginRequest;
import com.locallink.dto.request.RegisterRequest;
import com.locallink.dto.request.UpdateProfileRequest;
import com.locallink.dto.response.LoginResponse;
import com.locallink.dto.response.UserRegistrationResponse;
import com.locallink.entity.User;
import com.locallink.enums.Role;
import com.locallink.security.JwtService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.locallink.dto.response.ProfileResponse;
import com.locallink.repository.UserRepository;

@Service
public class UserServiceImpl {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;


    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,AuthenticationManager authenticationManager,JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtService = jwtService;
    }

    public UserRegistrationResponse registerUser(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        User user = new User();


        user.setFullName(request.getName());
        user.setEmail(request.getEmail());
        user.setPhoneNumber(request.getPhoneNumber());
        user.setRole(Role.CUSTOMER);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        // Save user to database
        User savedUser = userRepository.save(user);

        // Create response object
        UserRegistrationResponse response = new UserRegistrationResponse();


        response.setId(savedUser.getId());
        response.setName(savedUser.getFullName());
        response.setEmail(savedUser.getEmail());
        response.setPhone(savedUser.getPhoneNumber());
        response.setRole(savedUser.getRole().toString());
        response.setMessage("User registered successfully");

        return response;
    }

    public LoginResponse loginUser(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(
                        new UsernamePasswordAuthenticationToken(
                                request.getEmail(),
                                request.getPassword()
                        )
                );
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String token = jwtService.generateToken(userDetails);
        return new LoginResponse("Login successful", token);
    }

    public ProfileResponse getProfile(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        ProfileResponse response = new ProfileResponse();

        response.setId(user.getId());
        response.setFullName(user.getFullName());
        response.setEmail(user.getEmail());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setRole(user.getRole().toString());

        return response;
    }

    public ProfileResponse updateProfile(String email, UpdateProfileRequest request) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        user.setFullName(request.getFullName());
        user.setPhoneNumber(request.getPhoneNumber());

        User updatedUser = userRepository.save(user);

        ProfileResponse response = new ProfileResponse();

        response.setId(updatedUser.getId());
        response.setFullName(updatedUser.getFullName());
        response.setEmail(updatedUser.getEmail());
        response.setPhoneNumber(updatedUser.getPhoneNumber());
        response.setRole(updatedUser.getRole().toString());

        return response;
    }
}
