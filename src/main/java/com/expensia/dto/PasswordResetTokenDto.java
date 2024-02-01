package com.expensia.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetTokenDto {
    private Long id;
    private String token;
    private Long userId;
    private LocalDateTime expiryDate;
}
