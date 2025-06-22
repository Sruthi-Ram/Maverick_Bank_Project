/**
 * -----------------------------------------------------------------------------
 * Author      : Sruthi Ramesh
 * Date        : May 20, 2025
 * Description : This class implements the UserService interface to handle 
 *               user registration, login, retrieval, update, and deletion 
 *               operations in the Maverick Bank application. It integrates 
 *               with Spring Security for authentication and uses JWT for 
 *               token generation.
 * -----------------------------------------------------------------------------
 */

package com.hexaware.maverickbank.service.implementations;

//import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.log;

import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hexaware.maverickbank.dto.LoginRequestDTO;
import com.hexaware.maverickbank.dto.UserDTO;
import com.hexaware.maverickbank.dto.UserRegistrationRequestDTO;
import com.hexaware.maverickbank.dto.UserUpdateRequestDTO;
import com.hexaware.maverickbank.dto.entity.Role;
import com.hexaware.maverickbank.dto.entity.User;
import com.hexaware.maverickbank.repository.IRoleRepository;
import com.hexaware.maverickbank.repository.IUserRepository;
import com.hexaware.maverickbank.security.JwtService;
import com.hexaware.maverickbank.service.interfaces.UserService;

import jakarta.validation.ValidationException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private IUserRepository userRepository;

	@Autowired
	private IRoleRepository roleRepository;

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private JwtService jwtService;

	private boolean isValidPassword(String password) {
		Pattern pattern = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");
		Matcher matcher = pattern.matcher(password);
		return matcher.matches();
	}

	private boolean isValidEmail(String email) {
		// Password must contain at least one uppercase letter, one lowercase letter,
		// one digit, and must be at least 8 characters long
		Pattern pattern = Pattern
				.compile("^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z-]+\\.)+[a-zA-Z]{2,7}$");
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	@Override
	public UserDTO registerUser(UserRegistrationRequestDTO registrationRequestDTO) {
		log.info("Registering user with username: {}", registrationRequestDTO.getUsername());

		if (userRepository.findByUsername(registrationRequestDTO.getUsername()) != null) {
			log.warn("Username already exists: {}", registrationRequestDTO.getUsername());
			throw new ValidationException("Username already exists");
		}

		if (!isValidEmail(registrationRequestDTO.getEmail())) {
			log.warn("Invalid email format: {}", registrationRequestDTO.getEmail());
			throw new ValidationException("Invalid email format");
		}

		if (userRepository.findByEmail(registrationRequestDTO.getEmail()) != null) {
			log.warn("Email already exists: {}", registrationRequestDTO.getEmail());
			throw new ValidationException("Email already exists");
		}

		if (!isValidPassword(registrationRequestDTO.getPassword())) {
			log.warn("Invalid password format for username: {}", registrationRequestDTO.getUsername());
			throw new ValidationException(
					"Password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one digit");
		}

		User user = new User();
		user.setUsername(registrationRequestDTO.getUsername());
		user.setPassword(passwordEncoder.encode(registrationRequestDTO.getPassword()));
		user.setEmail(registrationRequestDTO.getEmail());

		if (registrationRequestDTO.getRoleId() != null) {
			Role role = roleRepository.findById(registrationRequestDTO.getRoleId().longValue())
					.orElse(roleRepository.findByName("CUSTOMER"));
			user.setRole(role);
		} else {
			Role defaultRole = roleRepository.findByName("CUSTOMER");
			user.setRole(defaultRole);
		}

		User savedUser = userRepository.save(user);
		UserDTO userDTO = convertUserToDTO(savedUser);
		log.info("User registered successfully with ID: {}", userDTO.getUserId());
		return userDTO;
	}

	@Override
	public String login(LoginRequestDTO loginRequestDTO) {
		try {

			Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					loginRequestDTO.getUsername(), loginRequestDTO.getPassword()));

			UserDetails userDetails = (UserDetails) authentication.getPrincipal();
			String jwtToken = jwtService.generateToken(userDetails);
			log.info("User logged in successfully: {}", loginRequestDTO.getUsername());
			return jwtToken; // Return token as String
		} catch (Exception ex) {
			log.warn("Login failed for user: {}", loginRequestDTO.getUsername());
			throw new ValidationException("Invalid username or password");
		}
	}

	@Override
	public UserDTO getUserById(Long userId) {
		log.info("Fetching user by ID: {}", userId);
		User user = userRepository.findById(userId).orElseThrow(() -> {
			log.warn("User not found with ID: {}", userId);
			return new NoSuchElementException("User not found with ID: " + userId);
		});
		UserDTO userDTO = convertUserToDTO(user);
		log.info("User found with ID: {}", userDTO.getUserId());
		return userDTO;
	}

	@Override
	public UserDTO updateUser(Long userId, UserUpdateRequestDTO updateRequestDTO) {
	    log.info("Updating user with ID: {}", userId);

	    User user = userRepository.findById(userId).orElseThrow(() -> {
	        log.warn("User not found with ID: {}", userId);
	        return new NoSuchElementException("User not found with ID: " + userId);
	    });

	    boolean updated = false;

	    // ✅ Update username
	    if (updateRequestDTO.getUsername() != null && !updateRequestDTO.getUsername().isEmpty()
	            && !updateRequestDTO.getUsername().equals(user.getUsername())) {
	        if (userRepository.findByUsername(updateRequestDTO.getUsername()) != null) {
	            log.warn("Username already exists: {}", updateRequestDTO.getUsername());
	            throw new ValidationException("Username already exists");
	        }
	        user.setUsername(updateRequestDTO.getUsername());
	        updated = true;
	    }

	    // ✅ Update password
	    if (updateRequestDTO.getPassword() != null && !updateRequestDTO.getPassword().isEmpty()) {
	        if (!isValidPassword(updateRequestDTO.getPassword())) {
	            log.warn("Invalid new password format for user ID: {}", userId);
	            throw new ValidationException("New password must be at least 8 characters long and contain at least one uppercase letter, one lowercase letter, and one digit");
	        }
	        user.setPassword(passwordEncoder.encode(updateRequestDTO.getPassword()));
	        updated = true;
	    }

	    // ✅ Update email
	    if (updateRequestDTO.getEmail() != null && !updateRequestDTO.getEmail().isEmpty()
	            && !updateRequestDTO.getEmail().equals(user.getEmail())) {
	        if (!isValidEmail(updateRequestDTO.getEmail())) {
	            log.warn("Invalid new email format for user ID: {}", userId);
	            throw new ValidationException("Invalid email format");
	        }
	        if (userRepository.findByEmail(updateRequestDTO.getEmail()) != null) {
	            log.warn("Email already exists: {}", updateRequestDTO.getEmail());
	            throw new ValidationException("Email already exists");
	        }
	        user.setEmail(updateRequestDTO.getEmail());
	        updated = true;
	    }

	    // ✅ Update role
	    if (updateRequestDTO.getRoleId() != null) {
	        Role role = roleRepository.findById(updateRequestDTO.getRoleId()).orElseThrow(() -> {
	            log.warn("Role not found with ID: {}", updateRequestDTO.getRoleId());
	            return new NoSuchElementException("Role not found with ID: " + updateRequestDTO.getRoleId());
	        });
	        user.setRole(role);
	        updated = true;
	    }

	    UserDTO userDTO = updated ? convertUserToDTO(userRepository.save(user)) : convertUserToDTO(user);
	    log.info("User with ID {} updated successfully. Updated: {}", userId, updated);
	    return userDTO;
	}


	@Override
	public void deleteUser(Long userId) {
		log.info("Deleting user with ID: {}", userId);
		if (!userRepository.existsById(userId)) {
			log.warn("User not found with ID: {}", userId);
			throw new NoSuchElementException("User not found with ID: " + userId);
		}
		userRepository.deleteById(userId);
		log.info("User with ID {} deleted successfully.", userId);
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

	@Override
	public User getUserByUsername(String username) {
		return userRepository.findByUsername(username);
	}

}
