package com.expensia.service.impl;

import com.expensia.entity.User;
import com.expensia.repository.UserRepository;
import com.expensia.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new UsernameNotFoundException("User does not exist with given id"));
    }

    @Override
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User does not exist with given username"));
    }

    @Override
    public User getUserByUsernameOrEmail(String username, String email) {
        return userRepository.findByUsernameOrEmail(username, email).orElseThrow(() -> new UsernameNotFoundException("User does not exist with given username or email"));
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void updateUser(User user) {
        // Perform any necessary validations or business logic before updating
        if (user.getId() == null || !userRepository.existsById(user.getId())) {
            // Handle the case where the user doesn't exist or the ID is null
            throw new EntityNotFoundException("User not found for update");
        }
        // Update the user
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email){
        return userRepository.findByUsernameOrEmail(email, email).orElseThrow(() -> new UsernameNotFoundException("could not find"));
    }
}
