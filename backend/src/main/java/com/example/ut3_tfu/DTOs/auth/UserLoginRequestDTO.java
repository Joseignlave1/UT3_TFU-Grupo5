package com.example.ut3_tfu.DTOs.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class UserLoginRequestDTO {
    @NotBlank @Email @Size(max = 120)
    private String email;

    @NotBlank @Size(min = 6, max = 100)
    private String password;

    // Getters/Setters
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}
