package com.coldie.kitchenstocks.auth.service;

import com.coldie.kitchenstocks.auth.request.AuthenticationRequest;
import com.coldie.kitchenstocks.auth.request.RegisterRequest;
import com.coldie.kitchenstocks.auth.response.AuthenticationResponse;

public interface AuthenticationService {
    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse authenticate(AuthenticationRequest request);
}
