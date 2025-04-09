package com.jobportal.onlinejobportal.controller;

import com.jobportal.onlinejobportal.model.User;
import com.jobportal.onlinejobportal.repository.UserRepository;
import com.jobportal.onlinejobportal.security.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // ✅ Constructor-Based Dependency Injection
    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // ✅ User Login
    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody User loginRequest) {
        Optional<User> user = userRepository.findByEmail(loginRequest.getEmail());

        if (user.isPresent() && passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword())) {
            String token = jwtUtil.generateToken(user.get().getEmail(), user.get().getRole());

            // ✅ Send Proper JSON Response
            Map<String, String> response = new HashMap<>();
            response.put("token", token);
            response.put("role", user.get().getRole());

            return ResponseEntity.ok(response);
        }
        return ResponseEntity.status(403).body(Map.of("error", "Invalid email or password"));
    }

    // ✅ User Registration
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            return ResponseEntity.badRequest().body(Map.of("error", "Email already in use"));
        }

        // ✅ Assign Default Role (USER, RECRUITER, ADMIN)
        if (user.getRole() == null || user.getRole().isEmpty()) {
            user.setRole("USER");  // Default to USER
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
        return ResponseEntity.ok(Map.of("message", "User registered successfully"));
    }

    // ✅ Get User Info from JWT
    @GetMapping("/userinfo")
    public ResponseEntity<?> getUserInfo(@RequestHeader("Authorization") String token) {
        try {
            token = token.substring(7); // Remove "Bearer "
            String email = jwtUtil.extractEmail(token);
            Optional<User> user = userRepository.findByEmail(email);

            if (user.isPresent()) {
                Map<String, String> response = new HashMap<>();
                response.put("email", user.get().getEmail());
                response.put("role", user.get().getRole());
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.status(404).body(Map.of("error", "User not found"));
            }
        } catch (Exception e) {
            return ResponseEntity.status(403).body(Map.of("error", "Invalid token"));
        }
    }
}
