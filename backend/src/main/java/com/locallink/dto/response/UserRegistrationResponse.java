package com.locallink.dto.response;


import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationResponse {
    private Long id;
    private String name;
    private String email;
    private String phone;
    private String role;
    private String message;
}
