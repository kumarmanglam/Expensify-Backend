package com.expensia.service.impl;

import com.expensia.dto.ChangePasswordDto;
import com.expensia.dto.JwtAuthResponse;
import com.expensia.dto.LoginDto;
import com.expensia.dto.RegisterDto;
import com.expensia.entity.Role;
import com.expensia.entity.User;
import com.expensia.exception.*;
import com.expensia.repository.RoleRepository;
import com.expensia.repository.UserRepository;
import com.expensia.security.JwtTokenProvider;
import com.expensia.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public String register(RegisterDto registerDto) {
        //check if username and password already exists in database.
        if (userRepository.existsByUsername(registerDto.getUsername())) {
            throw new DuplicateUsernameException("Username already exists");
        }

        if (userRepository.existsByEmail(registerDto.getEmail())) {
            throw new DuplicateEmailException("Email already exists");
        }

        //create a user object and save that in database.
        User user = new User();
        user.setName(registerDto.getName());
        user.setUsername(registerDto.getUsername());
        user.setEmail(registerDto.getEmail());
        user.setPassword(passwordEncoder.encode(registerDto.getPassword()));

        //assign a role to user
        Set<Role> roles = new HashSet<>();
        Role userRole = roleRepository.findByName("USER_ROLE");
        roles.add(userRole);
        user.setRoles(roles);
        userRepository.save(user);

        return "User Registered Successfully";
    }

    @Override
    public JwtAuthResponse login(LoginDto loginDto) {
        // get authentication object from auth
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getUsernameOrEmail(),
                loginDto.getPassword()
        ));
        SecurityContextHolder.getContext().setAuthentication(authenticate);

        String token = jwtTokenProvider.generateToken(authenticate);

        // get the role of logged in user from database
        Optional<User> userOptional = userRepository.findByUsernameOrEmail(
                loginDto.getUsernameOrEmail(), loginDto.getUsernameOrEmail());

        String role = null;
        if(userOptional.isPresent()){
            User loggedIn = userOptional.get();
            Optional<Role> optionalRole = loggedIn.getRoles().stream().findFirst();
            if(optionalRole.isPresent()){
                Role userRole = optionalRole.get();
                role = userRole.getName();
            }
            JwtAuthResponse jwtAuthResponse = new JwtAuthResponse();
            jwtAuthResponse.setRole(role);
            jwtAuthResponse.setAccessToken(token);
            return jwtAuthResponse;
        }

        throw new UserNotFoundException("User not found");
    }

    @Override
    public String changePassword(ChangePasswordDto changePasswordDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
//        System.out.println("the username is: " + username);
        User user = userRepository.findByUsernameOrEmail(username,username)
                .orElseThrow(() -> new ResourceNotFoundException("No user found with the given username"));

        System.out.println("============= the old credent and new credentials are +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++=");
        System.out.println(user.getPassword());
        System.out.println();
        System.out.println(changePasswordDto.getOldPassword());
        if(passwordEncoder.matches( changePasswordDto.getOldPassword(),user.getPassword())){
            user.setPassword(passwordEncoder.encode(changePasswordDto.getNewPassword()));
            userRepository.save(user);
            return "Password changed successfully";
        }else{
            throw new RuntimeException("Incorrect old password");
        }
    }
}

//The AuthService implementation performs user registration and login using Spring Security.
// It checks for duplicate usernames and emails during registration, encrypts passwords,
// assigns a user role, and generates a JWT token upon successful login for subsequent authentication