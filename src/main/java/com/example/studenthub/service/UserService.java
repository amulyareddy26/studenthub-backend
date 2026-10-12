package com.example.studenthub.service;

import com.example.studenthub.dto.*;
import java.util.List;

public interface UserService {
    List<UserResponse> list();
    UserResponse create(UserRequest request);
    UserResponse update(Long id, UserRequest request);
    void delete(Long id, String currentUsername);
}