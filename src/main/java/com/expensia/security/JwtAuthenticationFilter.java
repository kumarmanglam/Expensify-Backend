package com.expensia.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@AllArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private JwtTokenProvider jwtTokenProvider;

    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // Get JWT token http request
        String token = getTokenFromRequest(request);

        // Validate the token
        if(StringUtils.hasText(token) && jwtTokenProvider.validateToken(token)){
            // Extract username from the token
            String username = jwtTokenProvider.getUsername(token);
            // Load UserDetails based on the username
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // Create an authentication token with UserDetails and set it in SecurityContextHolder
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    userDetails,
                    null,
                    userDetails.getAuthorities()
            );
            authenticationToken.setDetails(
                    new WebAuthenticationDetailsSource()
                            .buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }
    // Extracts the JWT token from the "Authorization" header
    private String getTokenFromRequest(HttpServletRequest request){
        String bearerToken = request.getHeader("Authorization");
        if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")){
            return bearerToken.substring(7, bearerToken.length());
        }
        return null;
    }
}

//Filtering Process:
//
//The doFilterInternal method is part of the OncePerRequestFilter, ensuring that it is invoked only once per request.
//It extracts the JWT token from the request, validates it, and sets the authentication in the SecurityContextHolder.

//Bearer Token Extraction:
//
//The getTokenFromRequest method extracts the JWT token from the request's "Authorization" header, which is expected to be in the format "Bearer [token]".

//Request Chain Continuation:
//
//After authentication is set, the request continues along the filter chain

//Handling Missing or Invalid Tokens:
//
//The filter allows the request to proceed even if there is no token or if the token is invalid.
// This behavior might be appropriate depending on your use case. Ensure that downstream components are aware of the potential absence of authentication


