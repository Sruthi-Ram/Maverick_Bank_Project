package com.hexaware.maverickBank.service.interfaces;

import com.hexaware.maverickBank.dto.AuthRequest;
import com.hexaware.maverickBank.dto.AuthResponse;
import com.hexaware.maverickBank.dto.UserDTO;
import com.hexaware.maverickBank.dto.UserRegistrationRequestDTO;
import com.hexaware.maverickBank.dto.UserUpdateRequestDTO;

public interface UserService {
    UserDTO registerUser(UserRegistrationRequestDTO registrationRequestDTO); // Use DTO for registration request
    AuthResponse loginUser(AuthRequest authRequest);
    UserDTO getUserById(Long userId); // Return DTO
    UserDTO updateUser(Long userId, UserUpdateRequestDTO updateRequestDTO); // Use DTO for update request
   // void deactivateUser(Long userId);
    void deleteUser(Long userId);
}