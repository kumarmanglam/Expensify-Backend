package com.expensia.service.impl;

import com.expensia.entity.PasswordResetToken;
import com.expensia.entity.User;
import com.expensia.repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
public class PasswordResetTokenService {

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    public PasswordResetToken createToken(User user) {
        PasswordResetToken existingToken = tokenRepository.findByUser(user);

        // If an existing token is found, delete it
        if (existingToken != null) {
            tokenRepository.delete(existingToken);
        }
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        passwordResetToken.setExpiryDate(LocalDateTime.now().plusHours(1)); // Expiration time: 1 hour
        return tokenRepository.save(passwordResetToken);

        // if token already exists for same user then delete that token and re insert the new token
    }

    public PasswordResetToken findByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    public void deleteToken(PasswordResetToken token) {
        tokenRepository.delete(token);
    }
}


//UUID stands for Universally Unique Identifier.
// It is a 128-bit identifier that is guaranteed to be unique across both time and space
// Random UUID: d1b6c11b-61c7-4a22-b071-cb3c7a59c74b
