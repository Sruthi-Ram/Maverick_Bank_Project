package com.hexaware.maverickbank.service.interfaces;

import com.hexaware.maverickbank.dto.LoginRequestDTO;
import com.hexaware.maverickbank.dto.UserDTO;
import com.hexaware.maverickbank.dto.UserRegistrationRequestDTO;
import com.hexaware.maverickbank.dto.UserUpdateRequestDTO;
import com.hexaware.maverickbank.dto.entity.User;

public interface UserService {
    UserDTO registerUser(UserRegistrationRequestDTO registrationRequestDTO); // Use DTO for registration request
    UserDTO getUserById(Long userId); // Return DTO
    UserDTO updateUser(Long userId, UserUpdateRequestDTO updateRequestDTO); // Use DTO for update request
    
    void deleteUser(Long userId);
    String login(LoginRequestDTO loginRequestDTO);
	User getUserByUsername(String username);
}