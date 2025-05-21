package com.hexaware.maverickBank.service.interfaces;

import com.hexaware.maverickBank.entity.User;

public interface UserService {
    User registerUser(User user);
    User loginUser(String identifier, String password);
    User getUserById(int userId);
    User updateUser(int userId, String password, String email, String role, String status);
    void deactivateUser(int userId);
    void deleteUser(int userId);
}