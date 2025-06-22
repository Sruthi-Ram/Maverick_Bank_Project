package com.hexaware.maverickbank.service.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hexaware.maverickbank.dto.LoginRequestDTO;
import com.hexaware.maverickbank.dto.UserDTO;
import com.hexaware.maverickbank.dto.UserRegistrationRequestDTO;
import com.hexaware.maverickbank.dto.UserUpdateRequestDTO;
import com.hexaware.maverickbank.dto.entity.Role;
import com.hexaware.maverickbank.dto.entity.User;
import com.hexaware.maverickbank.repository.IRoleRepository;
import com.hexaware.maverickbank.repository.IUserRepository;
import com.hexaware.maverickbank.security.JwtService;

import jakarta.validation.ValidationException;

class UserServiceImplTest {

    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IRoleRepository roleRepository;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    private User user;
    private UserDTO userDTO;
    private UserRegistrationRequestDTO registrationRequestDTO;
    private LoginRequestDTO loginRequestDTO;
    private UserUpdateRequestDTO updateRequestDTO;
    private Role role;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        role = new Role();
        role.setRoleId(1L);
        role.setName("CUSTOMER");
        user = new User(1L, "testUser", "encodedPassword", "test@example.com", role, null, null);
        userDTO = new UserDTO(1L, "testUser", "test@example.com", 1L);
        registrationRequestDTO = new UserRegistrationRequestDTO();
        registrationRequestDTO.setUsername("newUser");
        registrationRequestDTO.setPassword("Password123");
        registrationRequestDTO.setEmail("new@example.com");
        registrationRequestDTO.setRoleId(1L);
        loginRequestDTO = new LoginRequestDTO();
        loginRequestDTO.setUsername("testUser");
        loginRequestDTO.setPassword("password");
        updateRequestDTO = new UserUpdateRequestDTO();
        userDetails = org.springframework.security.core.userdetails.User.withUsername("testUser").password("encodedPassword").roles("CUSTOMER").build();
    }

    @Test
    void testRegisterUser() {
        when(userRepository.findByUsername("newUser")).thenReturn(null);
        when(userRepository.findByEmail("new@example.com")).thenReturn(null);
        when(passwordEncoder.encode("Password123")).thenReturn("encodedPassword");
        when(roleRepository.findById(1L)).thenReturn(Optional.of(role));

        User newUserToSave = new User();
        newUserToSave.setUserId(1L); // Keep the ID consistent for now
        newUserToSave.setUsername("newUser");
        newUserToSave.setPassword("encodedPassword");
        newUserToSave.setEmail("new@example.com");
        newUserToSave.setRole(role);

        when(userRepository.save(any(User.class))).thenReturn(newUserToSave);

        UserDTO registeredUserDTO = userService.registerUser(registrationRequestDTO);
        assertNotNull(registeredUserDTO);
        assertEquals("newUser", registeredUserDTO.getUsername());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testRegisterUser_UsernameExists() {
        when(userRepository.findByUsername("newUser")).thenReturn(user);
        assertThrows(ValidationException.class, () -> userService.registerUser(registrationRequestDTO));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testLogin_Success() {
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(jwtService.generateToken(userDetails)).thenReturn("mockedToken");

        String token = userService.login(loginRequestDTO);
        assertEquals("mockedToken", token);
    }

    @Test
    void testLogin_InvalidCredentials() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new ValidationException("Invalid username or password"));
        assertThrows(ValidationException.class, () -> userService.login(loginRequestDTO));
    }

    @Test
    void testGetUserById_Found() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        UserDTO foundUserDTO = userService.getUserById(1L);
        assertNotNull(foundUserDTO);
        assertEquals(userDTO.getUserId(), foundUserDTO.getUserId());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> userService.getUserById(1L));
    }

    @Test
    void testUpdateUser_Found() {
        updateRequestDTO.setPassword("NewPassword123");
        updateRequestDTO.setEmail("updated@example.com");
        updateRequestDTO.setRoleId(2L);
        Role newRole = new Role();
        newRole.setRoleId(2L);
        newRole.setName("ADMIN");
        User updatedUser = new User();
        updatedUser.setUserId(1L);
        updatedUser.setUsername("testUser");
        updatedUser.setPassword("encodedNewPassword");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setRole(newRole);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("NewPassword123")).thenReturn("encodedNewPassword");
        when(roleRepository.findById(2L)).thenReturn(Optional.of(newRole));
        when(userRepository.findByEmail("updated@example.com")).thenReturn(null);
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserDTO updatedUserDTO = userService.updateUser(1L, updateRequestDTO);
        assertNotNull(updatedUserDTO);
        assertEquals("updated@example.com", updatedUserDTO.getEmail());
        assertEquals(2L, updatedUserDTO.getRoleId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> userService.updateUser(1L, updateRequestDTO));
        verify(userRepository, never()).save(any());
    }

    @Test
    void testDeleteUser_Found() {
        when(userRepository.existsById(1L)).thenReturn(true);
        userService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteUser_NotFound() {
        when(userRepository.existsById(1L)).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> userService.deleteUser(1L));
        verify(userRepository, never()).deleteById(any());
    }

    @Test
    void testGetUserByUsername_Found() {
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        User foundUser = userService.getUserByUsername("testUser");
        assertNotNull(foundUser);
        assertEquals("testUser", foundUser.getUsername());
    }

    @Test
    void testGetUserByUsername_NotFound() {
        when(userRepository.findByUsername("nonExistingUser")).thenReturn(null);
        assertNull(userService.getUserByUsername("nonExistingUser"));
    }
}