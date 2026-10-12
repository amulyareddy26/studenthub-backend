package com.example.studenthub.service.impl;

import com.example.studenthub.dto.UserRequest;
import com.example.studenthub.dto.UserResponse;
import com.example.studenthub.entity.User;
import com.example.studenthub.exception.BadRequestException;
import com.example.studenthub.exception.ConflictException;
import com.example.studenthub.exception.ResourceNotFoundException;
import com.example.studenthub.repository.RoleRepository;
import com.example.studenthub.repository.UserRepository;
import com.example.studenthub.service.UserService;
import java.util.List;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional(readOnly = true)
    public List<UserResponse> list() {
        return userRepository.findAll().stream().map(UserResponse::from).toList();
    }

    @Override
    @Transactional
    public UserResponse create(UserRequest r) {
        if (r.password() == null || r.password().isBlank()) throw new BadRequestException("Password is required");
        String username = r.username().trim();
        if (userRepository.existsByUsername(username)) throw new ConflictException("Username already exists");
        User u = new User();
        u.setUsername(username);
        u.setPasswordHash(passwordEncoder.encode(r.password()));
        u.setRole(roleRepository.findByRoleName(r.role()).orElseThrow(() -> new BadRequestException("Invalid role")));
        u.setStatus(r.status());
        return UserResponse.from(userRepository.save(u));
    }

    @Override
    @Transactional
    public UserResponse update(Long id, UserRequest r) {
        User u = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        String username = r.username().trim();
        userRepository.findByUsername(username).filter(o -> !o.getUserId().equals(id))
                .ifPresent(o -> { throw new ConflictException("Username already exists"); });
        u.setUsername(username);
        if (r.password() != null && !r.password().isBlank()) u.setPasswordHash(passwordEncoder.encode(r.password()));
        u.setRole(roleRepository.findByRoleName(r.role()).orElseThrow(() -> new BadRequestException("Invalid role")));
        u.setStatus(r.status());
        return UserResponse.from(userRepository.save(u));
    }

    @Override
    @Transactional
    public void delete(Long id, String currentUsername) {
        User u = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id " + id));
        if (u.getUsername().equals(currentUsername)) throw new BadRequestException("You cannot delete your own account");
        userRepository.delete(u);
    }
}