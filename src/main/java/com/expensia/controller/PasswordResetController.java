package com.expensia.controller;

import com.expensia.entity.PasswordResetToken;
import com.expensia.entity.User;
import com.expensia.service.UserService;
import com.expensia.service.impl.EmailService;
import com.expensia.service.impl.PasswordResetTokenService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.Map;


@CrossOrigin("*")
@RestController
@RequestMapping("/expensia/password-reset")
@AllArgsConstructor
public class PasswordResetController {
    @Autowired
    private UserService userService;

    @Autowired
    private PasswordResetTokenService tokenService;

    @Autowired
    private EmailService emailService;

    private PasswordEncoder passwordEncoder;
    @PostMapping("/request/{email}")
    public ResponseEntity<String> requestPasswordReset(@PathVariable String email) {
        User user = userService.findByEmail(email);
        if (user == null) {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }

        PasswordResetToken token = tokenService.createToken(user);

        String resetLink = "http://localhost:5173/resetPassword?token=" + token.getToken();
        String emailBody = "Click the link to reset your password: " + resetLink;

        emailService.sendPasswordResetEmail(email, "Password Reset Request", emailBody);

        return new ResponseEntity<>("Password reset email sent successfully", HttpStatus.OK);
    }

    @PostMapping("/reset/{token}")
    public ResponseEntity<String> resetPassword(
            @PathVariable("token") String token,
            @RequestBody Map<String, String> requestBody
    ) {
        String newPassword = requestBody.get("newPassword");
        System.out.println("received the token successfully = " + token);
        System.out.println("received the password successfully = " + newPassword);
        PasswordResetToken passwordResetToken = tokenService.findByToken(token);
        if (passwordResetToken == null || passwordResetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            return new ResponseEntity<>("Invalid or expired token", HttpStatus.BAD_REQUEST);
        }
        User user = passwordResetToken.getUser();
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.updateUser(user);
        tokenService.deleteToken(passwordResetToken);
        return new ResponseEntity<>("Password reset successfully", HttpStatus.OK);
    }
}
