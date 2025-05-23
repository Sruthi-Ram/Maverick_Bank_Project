package com.hexaware.maverickBank.service.implementations;

import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.hexaware.maverickBank.entity.Role;
import com.hexaware.maverickBank.entity.User;
import com.hexaware.maverickBank.repository.IRoleRepository;
import com.hexaware.maverickBank.repository.IUserRepository;
import com.hexaware.maverickBank.service.interfaces.UserService;

import jakarta.validation.ValidationException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private IRoleRepository roleRepository;

    private boolean isValidPassword(String password) {
        // Example password complexity rule: at least 8 characters, one uppercase, one lowercase, one digit
        Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");
        Matcher matcher = pattern.matcher(password);
        return matcher.matches();
    }

    private boolean isValidEmail(String email) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z-]+\\.)+[a-zA-Z]{2,7}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public User registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()) != null) {
            throw new ValidationException("Username already exists");
        }
        if (userRepository.findByEmail(user.getEmail()) != null) {
            throw new ValidationException("Email already exists");
        }
        if (!isValidPassword(user.getPassword())) {
            throw new ValidationException("Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one digit");
        }
        if (!isValidEmail(user.getEmail())) {
            throw new ValidationException("Invalid email format");
        }
        return userRepository.save(user);
    }

    @Override
    public User loginUser(String identifier, String password) {
        User user = userRepository.findByUsernameOrEmail(identifier);
        if (user == null || !user.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        return user;
    }

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
    }

    @Override
    public User updateUser(Long userId, String password, String email, String roleName, String status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        boolean updated = false;
        if (password != null && !password.isEmpty()) {
            if (!isValidPassword(password)) {
                throw new ValidationException("New password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one digit");
            }
            user.setPassword(password);
            updated = true;
        }
        if (email != null && !email.isEmpty() && !email.equals(user.getEmail())) {
            if (!isValidEmail(email)) {
                throw new ValidationException("Invalid email format");
            }
            if (userRepository.findByEmail(email) != null) {
                throw new ValidationException("Email already exists");
            }
            user.setEmail(email);
            updated = true;
        }
        if (roleName != null && !roleName.isEmpty()) {
            Role role = roleRepository.findByName(roleName);
            if (role != null) {
                user.setRole(role);
                updated = true;
            } else {
                throw new NoSuchElementException("Role not found with name: " + roleName);
            }
        }
        // Implement status update logic based on allowed statuses if needed
        return updated ? userRepository.save(user) : user;
    }

    @Override
    public void deactivateUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        // Implement deactivation logic (e.g., set status to inactive)
        // user.setStatus("INACTIVE");
        userRepository.save(user);
    }

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User not found with ID: " + userId);
        }
        // Consider adding logic to prevent deleting users with associated data
        userRepository.deleteById(userId);
    }
}