package com.ems.controller;

import com.ems.model.Admin;
import com.ems.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admins")
@CrossOrigin(origins = {
        "http://localhost:5173",  // frontend dev
        "http://localhost:9090"   // frontend deployed in Tomcat
})
public class AdminController {

    @Autowired
    private AdminRepository adminRepository;

    // Create Admin
    @PostMapping("/create")
    public Admin createAdmin(@RequestBody Map<String, String> userData) {
        Admin admin = new Admin();

        String name = userData.get("name");
        if (name == null || name.trim().isEmpty()) {
            throw new RuntimeException("Name is required");
        }
        admin.setFullName(name);

        admin.setUsername(userData.get("username"));
        admin.setPassword(userData.get("password"));
        admin.setEmail(userData.get("email"));

        // Validation
        if (admin.getEmail() == null || admin.getEmail().trim().isEmpty()) {
            throw new RuntimeException("Email is required");
        }
        if (admin.getUsername() == null || admin.getUsername().trim().isEmpty()) {
            throw new RuntimeException("Username is required");
        }
        if (admin.getPassword() == null || admin.getPassword().trim().isEmpty()) {
            throw new RuntimeException("Password is required");
        }

        if (adminRepository.findByEmail(admin.getEmail()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }
        if (adminRepository.findByUsername(admin.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        return adminRepository.save(admin);
    }

    // Admin login
    @PostMapping("/login")
    public Admin loginAdmin(@RequestBody Map<String, String> payload) {
        String email = payload.get("loginId");
        String password = payload.get("password");
        return adminRepository.findByEmail(email)
                .filter(a -> a.getPassword().equals(password))
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));
    }

    // Get all admins
    @GetMapping
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }
}
