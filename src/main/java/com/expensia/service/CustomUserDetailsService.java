package com.expensia.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface CustomUserDetailsService {
    UserDetails loadUserByUsername(String username);
}

// This interface is a part of the Spring Security framework, and
// you typically provide your own implementation of this interface to load user details during the authentication process