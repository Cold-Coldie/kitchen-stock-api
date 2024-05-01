package com.coldie.kitchenstocks.config;

import com.coldie.kitchenstocks.exception.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Collections;

@Component("customAuthenticationEntryPoint")
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException)
            throws IOException, ServletException {

        ErrorResponse errorResponse;
        if (authException != null) {
            errorResponse = new ErrorResponse(String.valueOf(HttpStatus.UNAUTHORIZED.value()),
                    HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                    Collections.singletonList("User not authenticated. Please re-authenticate."));
        } else {
            errorResponse = new ErrorResponse(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR.value()),
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    Collections.singletonList("An unexpected error occurred."));
        }

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
    }
}
