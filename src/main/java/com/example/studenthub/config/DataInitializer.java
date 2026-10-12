package com.example.studenthub.config;

import com.example.studenthub.entity.Department;
import com.example.studenthub.entity.Role;
import com.example.studenthub.entity.User;
import com.example.studenthub.repository.DepartmentRepository;
import com.example.studenthub.repository.RoleRepository;
import com.example.studenthub.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${app.admin.username}")
    private String adminUsername;
    @Value("${app.admin.password}")
    private String adminPassword;

    public DataInitializer(RoleRepository roleRepository, UserRepository userRepository,
                           DepartmentRepository departmentRepository, PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        for (String r : List.of("ADMIN", "FACULTY", "STUDENT")) {
            if (roleRepository.findByRoleName(r).isEmpty()) roleRepository.save(new Role(r));
        }
        if (!userRepository.existsByUsername(adminUsername)) {
            User u = new User();
            u.setUsername(adminUsername);
            u.setPasswordHash(passwordEncoder.encode(adminPassword));
            u.setRole(roleRepository.findByRoleName("ADMIN").orElseThrow());
            u.setStatus("ACTIVE");
            userRepository.save(u);
        }
        if (departmentRepository.count() == 0) {
            departmentRepository.saveAll(List.of(
                    new Department("CSE", "Computer Science and Engineering"),
                    new Department("IT", "Information Technology"),
                    new Department("ECE", "Electronics and Communication Engineering"),
                    new Department("EEE", "Electrical and Electronics Engineering"),
                    new Department("MECH", "Mechanical Engineering"),
                    new Department("CIVIL", "Civil Engineering")));
        }
    }
}