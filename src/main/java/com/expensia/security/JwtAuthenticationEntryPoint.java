package com.expensia.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
    }
}

// The purpose of an AuthenticationEntryPoint is to handle authentication failures,
// particularly in scenarios where an unauthenticated request tries to access a secured resource.

// The commence method is called when an unauthenticated user attempts to access a secured resource, and their request lacks valid authentication credentials.
// In your implementation, when such an event occurs, the method sends an HTTP 401 Unauthorized response to the client

// It's common to use an AuthenticationEntryPoint in conjunction with Spring Security and
// JWT (JSON Web Token) authentication to handle unauthorized access and provide meaningful responses.
