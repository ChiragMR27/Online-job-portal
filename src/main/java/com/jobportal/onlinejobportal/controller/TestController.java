package com.jobportal.onlinejobportal.controller;

import com.jobportal.onlinejobportal.model.User;
import com.jobportal.onlinejobportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/test")
@CrossOrigin(origins = "http://localhost:3000")
public class TestController {
    
    @Autowired
    private UserRepository userRepository;
    
    @GetMapping("/ping")
    public String ping() {
        return "Server is running!";
    }
    
    @GetMapping("/check-db")
    public String checkDatabase() {
        try {
            List<User> users = userRepository.findAll();
            return "Database connection successful! Total users: " + users.size();
        } catch (Exception e) {
            return "Database connection failed: " + e.getMessage();
        }
    }
    
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
} 