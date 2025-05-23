package com.hexaware.maverickBank.service.implementations;

import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.hexaware.maverickBank.dto.UserDTO;
import com.hexaware.maverickBank.dto.UserRegistrationRequestDTO;
import com.hexaware.maverickBank.dto.UserUpdateRequestDTO;
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
    public UserDTO registerUser(UserRegistrationRequestDTO registrationRequestDTO) {
        if (userRepository.findByUsername(registrationRequestDTO.getUsername()) != null) {
            throw new ValidationException("Username already exists");
        }
        if (userRepository.findByEmail(registrationRequestDTO.getEmail()) != null) {
            throw new ValidationException("Email already exists");
        }
        if (!isValidPassword(registrationRequestDTO.getPassword())) {
            throw new ValidationException("Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one digit");
        }
        if (!isValidEmail(registrationRequestDTO.getEmail())) {
            throw new ValidationException("Invalid email format");
        }

        User user = new User();
        user.setUsername(registrationRequestDTO.getUsername());
        user.setPassword(registrationRequestDTO.getPassword());
        user.setEmail(registrationRequestDTO.getEmail());
        // Assuming default role for registration, you might need to fetch this based on logic
        Role defaultRole = roleRepository.findByName("CUSTOMER"); // Example: set default role to CUSTOMER
        user.setRole(defaultRole);

        User savedUser = userRepository.save(user);
        return convertUserToDTO(savedUser);
    }

    @Override
    public UserDTO loginUser(String identifier, String password) {
        User user = userRepository.findByUsernameOrEmail(identifier);
        if (user == null || !user.getPassword().equals(password)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid credentials");
        }
        return convertUserToDTO(user);
    }

    @Override
    public UserDTO getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        return convertUserToDTO(user);
    }

    @Override
    public UserDTO updateUser(Long userId, UserUpdateRequestDTO updateRequestDTO) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + userId));
        boolean updated = false;
        if (updateRequestDTO.getPassword() != null && !updateRequestDTO.getPassword().isEmpty()) {
            if (!isValidPassword(updateRequestDTO.getPassword())) {
                throw new ValidationException("New password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one digit");
            }
            user.setPassword(updateRequestDTO.getPassword());
            updated = true;
        }
        if (updateRequestDTO.getEmail() != null && !updateRequestDTO.getEmail().isEmpty() && !updateRequestDTO.getEmail().equals(user.getEmail())) {
            if (!isValidEmail(updateRequestDTO.getEmail())) {
                throw new ValidationException("Invalid email format");
            }
            if (userRepository.findByEmail(updateRequestDTO.getEmail()) != null) {
                throw new ValidationException("Email already exists");
            }
            user.setEmail(updateRequestDTO.getEmail());
            updated = true;
        }
        if (updateRequestDTO.getRoleId() != null) {
            Role role = roleRepository.findById(updateRequestDTO.getRoleId())
                    .orElseThrow(() -> new NoSuchElementException("Role not found with ID: " + updateRequestDTO.getRoleId()));
            user.setRole(role);
            updated = true;
        }
        return updated ? convertUserToDTO(userRepository.save(user)) : convertUserToDTO(user);
    }

    

    @Override
    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NoSuchElementException("User not found with ID: " + userId);
        }
        userRepository.deleteById(userId);
    }

    private UserDTO convertUserToDTO(User user) {
        UserDTO dto = new UserDTO();
        dto.setUserId(user.getUserId());
        dto.setUsername(user.getUsername());
        dto.setEmail(user.getEmail());
        if (user.getRole() != null) {
            dto.setRoleId(user.getRole().getRoleId());
        }
        return dto;
    }
}