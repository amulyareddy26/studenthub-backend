package com.example.studenthub.service.impl;

import com.example.studenthub.dto.*;
import com.example.studenthub.entity.User;
import com.example.studenthub.exception.BadRequestException;
import com.example.studenthub.exception.ConflictException;
import com.example.studenthub.repository.RoleRepository;
import com.example.studenthub.repository.StudentRepository;
import com.example.studenthub.repository.UserRepository;
import com.example.studenthub.security.JwtService;
import com.example.studenthub.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final StudentRepository studentRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Value("${app.registration-enabled:true}")
    private boolean registrationEnabled;

    public AuthServiceImpl(UserRepository userRepository, RoleRepository roleRepository, StudentRepository studentRepository,
                           PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Override
    public LoginResponse login(LoginRequest req) {
        User u = userRepository.findByUsername(req.username().trim())
                .orElseThrow(() -> new BadCredentialsException("Invalid username or password"));
        if (!passwordEncoder.matches(req.password(), u.getPasswordHash())) {
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!"ACTIVE".equals(u.getStatus())) {
            throw new BadCredentialsException("Account is inactive. Contact admin.");
        }
        String role = u.getRole().getRoleName();
        return new LoginResponse(jwtService.generateToken(u.getUsername(), role), u.getUsername(), role);
    }

    @Override
    public MessageResponse register(RegisterRequest req) {
        if (!registrationEnabled) throw new BadRequestException("Registration is disabled");
        String rollNo = req.username().trim();
        studentRepository.findByRollNoIgnoreCase(rollNo)
                .orElseThrow(() -> new BadRequestException("No student found with this roll number. Ask admin to add you first."));
        if (userRepository.existsByUsername(rollNo)) throw new ConflictException("Account already exists for this roll number");
        User u = new User();
        u.setUsername(rollNo);
        u.setPasswordHash(passwordEncoder.encode(req.password()));
        u.setRole(roleRepository.findByRoleName("STUDENT").orElseThrow());
        u.setStatus("ACTIVE");
        userRepository.save(u);
        return new MessageResponse("Registered successfully. You can login now.");
    }
}