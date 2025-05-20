package com.hexaware.maverickBank.service.interfaces;

import com.hexaware.maverickBank.entity.User;

public interface UserService {
    User findByUsername(String username);
    User findByEmail(String email);
    User saveUser(User user);
    // Additional methods for user management by administrators
}