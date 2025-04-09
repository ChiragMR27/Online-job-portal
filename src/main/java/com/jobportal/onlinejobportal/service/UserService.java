package com.jobportal.onlinejobportal.service;

import com.jobportal.onlinejobportal.dto.UserDto;

public interface UserService {
    void registerUser(UserDto userDto);
    String loginUser(UserDto userDto);
    String generateResetToken(String email);
    boolean saveNewPassword(String token, String newPassword);
    UserDto getUserByEmail(String email);
    void updateUser(String id, UserDto userDto);
}
