package com.hexaware.maverickBank.service.interfaces;

import com.hexaware.maverickBank.entity.User;

public interface UserService {
    User registerUser(User user);
    User loginUser(String identifier, String password);
    User getUserById(Long userId);
    User updateUser(Long userId, String password, String email, String role, String status);
    void deactivateUser(Long userId);
    void deleteUser(Long userId);
}