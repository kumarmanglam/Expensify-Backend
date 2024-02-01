package com.expensia.service.impl;

import com.expensia.entity.User;
import com.expensia.repository.UserRepository;
import com.expensia.service.CustomUserDetailsService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CustomUserDetailsServiceImpl implements UserDetailsService {
    private UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsernameOrEmail(username, username)
                .orElseThrow(() -> new UsernameNotFoundException("user doesnt exist"));
        List<SimpleGrantedAuthority> grantedAuthorities = user.getRoles().stream().map((role) -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());

        return new org.springframework.security.core.userdetails.User(
                username,
                user.getPassword(),
                grantedAuthorities
        );
    }
}
// The UserDetails interface represents core user information. It contains methods such as getUsername(), getPassword(), getAuthorities(), and more.

// The UserDetailsService interface is responsible for retrieving user-related data when requested during the authentication process.
// It has a single method: loadUserByUsername(String username), which returns a UserDetails object for a given username

// GrantedAuthority Interface:
//
// This interface represents an authority granted to an authentication object.
// The primary method in this interface is getAuthority(), which returns the name of the authority.

// SimpleGrantedAuthority Class:
//
// A straightforward implementation of the GrantedAuthority interface.
// It takes a single string argument in its constructor, which represents the authority or role.

