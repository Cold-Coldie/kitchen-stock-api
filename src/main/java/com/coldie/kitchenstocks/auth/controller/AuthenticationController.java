package com.coldie.kitchenstocks.auth.controller;

import com.coldie.kitchenstocks.auth.request.AuthenticationRequest;
import com.coldie.kitchenstocks.auth.request.RegisterRequest;
import com.coldie.kitchenstocks.auth.response.AuthenticationResponse;
import com.coldie.kitchenstocks.auth.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        return new ResponseEntity<AuthenticationResponse>(authenticationService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody AuthenticationRequest request) {
        return new ResponseEntity<AuthenticationResponse>(authenticationService.authenticate(request), HttpStatus.OK);
    }

}
