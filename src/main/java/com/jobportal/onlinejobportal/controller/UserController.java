package com.jobportal.onlinejobportal.controller;

import com.jobportal.onlinejobportal.dto.UserDto;
import com.jobportal.onlinejobportal.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")  // Changed from /api/auth to /api/user
@CrossOrigin(origins = "http://localhost:3000") // Allow frontend access
public class UserController {

    @Autowired
    private UserService userService;

    // ✅ Get user by email
    @GetMapping("/{email}")
    public UserDto getUserByEmail(@PathVariable String email) {
        return userService.getUserByEmail(email);
    }

    // ✅ Update user details
    @PutMapping("/{id}")
    public String updateUser(@PathVariable String id, @RequestBody UserDto userDto) {
        userService.updateUser(id, userDto);
        return "User updated successfully!";
    }
}
