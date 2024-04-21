package com.coldie.kitchenstocks.auth.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public class AuthenticationRequest {
    @NotNull(message = "email cannot be null")
    @Email(message = "Please provide a valid email")
    private String email;

    @NotNull(message = "password cannot be null")
    private String password;

    public AuthenticationRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public AuthenticationRequest() {
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "AuthenticationRequest{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
