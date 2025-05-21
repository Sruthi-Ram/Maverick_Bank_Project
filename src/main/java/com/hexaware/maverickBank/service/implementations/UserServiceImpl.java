package com.hexaware.maverickBank.service.implementations;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hexaware.maverickBank.entity.User;
import com.hexaware.maverickBank.repository.IUserRepository;
import com.hexaware.maverickBank.service.interfaces.UserService;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    @Transactional
    public User registerUser(User user) {
        user.setCreatedAt(LocalDateTime.now());
        user.setUpdatedAt(LocalDateTime.now());
        user.setStatus("active");
        return userRepository.save(user);
    }

    @Override
    public User loginUser(String identifier, String password) {
        User user = userRepository.findByUsername(identifier);
        if (user == null) {
            user = userRepository.findByEmail(identifier);
        }
        if (user != null && user.getPassword().equals(password) && user.getStatus().equals("active")) {
            return user;
        }
        return null;
    }

    @Override
    public User getUserById(int userId) {
        return userRepository.findById(userId).orElse(null);
    }

    @Override
    @Transactional
    public User updateUser(int userId, String password, String email, String role, String status) {
        User existingUser = userRepository.findById(userId).orElse(null);
        if (existingUser != null) {
            userRepository.updateUser(userId, password, email, role, status);
            existingUser = userRepository.findById(userId).orElse(null);
            return existingUser;
        }
        return null;
    }

    @Override
    @Transactional
    public void deactivateUser(int userId) {
        userRepository.deactivateUser(userId);
    }

    @Override
    @Transactional
    public void deleteUser(int userId) {
        userRepository.deleteUserById(userId);
    }
}