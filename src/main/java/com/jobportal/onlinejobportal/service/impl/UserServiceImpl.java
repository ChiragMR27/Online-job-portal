	package com.jobportal.onlinejobportal.service.impl;
	
	import com.jobportal.onlinejobportal.dto.UserDto;
	import com.jobportal.onlinejobportal.model.User;
	import com.jobportal.onlinejobportal.repository.UserRepository;
	import com.jobportal.onlinejobportal.security.JwtUtil;
	import com.jobportal.onlinejobportal.service.UserService;
	import com.jobportal.onlinejobportal.service.EmailService;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
	import org.springframework.stereotype.Service;
	
	import java.util.Optional;
	
	@Service
	public class UserServiceImpl implements UserService {
	
	    @Autowired
	    private UserRepository userRepository;
	
	    @Autowired
	    private BCryptPasswordEncoder passwordEncoder;
	
	    @Autowired
	    private JwtUtil jwtUtil;
	
	    @Autowired
	    private EmailService emailService;
	
	    // ✅ Register User
	    @Override
	    public void registerUser(UserDto userDto) {
	        if (userRepository.existsByEmail(userDto.getEmail())) {
	            throw new RuntimeException("Email already in use");
	        }
	
	        User user = new User();
	        user.setEmail(userDto.getEmail());
	        user.setUsername(userDto.getUsername());
	        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
	        user.setRole("USER");
	
	        userRepository.save(user);
	    }
	
	    // ✅ Login User
	    @Override
	    public String loginUser(UserDto userDto) {
	        Optional<User> user = userRepository.findByEmail(userDto.getEmail());
	        if (user.isPresent() && passwordEncoder.matches(userDto.getPassword(), user.get().getPassword())) {
	            String role = user.get().getRole();  // Fetch role from User entity
	            return jwtUtil.generateToken(user.get().getEmail(), role);  // Pass both email and role
	        } else {
	            throw new RuntimeException("Invalid email or password");
	        }
	    }
	
	
	    // ✅ Get User by Email
	    @Override
	    public UserDto getUserByEmail(String email) {
	        Optional<User> user = userRepository.findByEmail(email);
	        if (user.isPresent()) {
	            UserDto userDto = new UserDto();
	            userDto.setEmail(user.get().getEmail());
	            userDto.setUsername(user.get().getUsername());
	            return userDto;
	        } else {    
	            throw new RuntimeException("User not found");
	        }
	    }
	
	    // ✅ Update User
	    @Override
	    public void updateUser(String id, UserDto userDto) {
	        Optional<User> user = userRepository.findById(id);
	        if (user.isPresent()) {
	            User existingUser = user.get();
	            existingUser.setUsername(userDto.getUsername());
	            existingUser.setEmail(userDto.getEmail());
	
	            if (userDto.getPassword() != null && !userDto.getPassword().isEmpty()) {
	                existingUser.setPassword(passwordEncoder.encode(userDto.getPassword()));
	            }
	
	            userRepository.save(existingUser);
	        } else {
	            throw new RuntimeException("User not found");
	        }
	    }
	
	    // ✅ Generate Reset Token
	    @Override
	    public String generateResetToken(String email) {
	        Optional<User> user = userRepository.findByEmail(email);
	        if (user.isPresent()) {
	        	String token = jwtUtil.generateToken(email, "RESET_PASSWORD");
	
	            emailService.sendEmail(email, "Password Reset",
	                    "Click the link to reset your password: http://localhost:3000/reset-password?token=" + token);
	            return token;
	        }
	        throw new RuntimeException("User not found");
	    }
	
	    // ✅ Save New Password
	    @Override
	    public boolean saveNewPassword(String token, String newPassword) {
	        String email = jwtUtil.extractEmail(token);
	        Optional<User> user = userRepository.findByEmail(email);
	        if (user.isPresent()) {
	            user.get().setPassword(passwordEncoder.encode(newPassword));
	            userRepository.save(user.get());
	            return true;
	        }
	        return false;
	    }
	}
