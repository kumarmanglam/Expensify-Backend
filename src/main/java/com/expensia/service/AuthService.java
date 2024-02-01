package com.expensia.service;

import com.expensia.dto.ChangePasswordDto;
import com.expensia.dto.JwtAuthResponse;
import com.expensia.dto.LoginDto;
import com.expensia.dto.RegisterDto;

public interface AuthService {
    String register(RegisterDto registerDto);

    JwtAuthResponse login(LoginDto loginDto);

    String changePassword(ChangePasswordDto changePasswordDto);
}

