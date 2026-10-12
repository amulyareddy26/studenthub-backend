package com.example.studenthub.service;

import com.example.studenthub.dto.*;

public interface AuthService {
    LoginResponse login(LoginRequest request);
    MessageResponse register(RegisterRequest request);
}