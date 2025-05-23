package com.hexaware.maverickBank.service.implementations;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.hexaware.maverickBank.entity.Role;
import com.hexaware.maverickBank.entity.User;
import com.hexaware.maverickBank.repository.IRoleRepository;
import com.hexaware.maverickBank.repository.IUserRepository;
import com.hexaware.maverickBank.service.interfaces.UserService;

@SpringBootTest
public class UserServiceImplTest {

	 @Mock
    private IUserRepository userRepository;

	 @InjectMocks
	 private UserServiceImpl userService;

    @Mock
    private IRoleRepository roleRepository; // If you need to set roles

    @BeforeEach
    void setUp() {
        // Insert test data before each test
        Role userRole = roleRepository.findByName("CUSTOMER"); // Assuming you have a Role entity and repository
        if (userRole == null) {
            userRole = new Role();
            userRole.setName("CUSTOMER");
            roleRepository.save(userRole);
        }

        User user = new User();
        user.setUsername("testuser");
        user.setEmail("test@example.com");
        user.setPassword("Pass123!");
        user.setRole(userRole);
        userRepository.save(user);
    }

    @Test
    void registerUser_Success() {
        User user = new User();
        user.setUsername("newuser");
        user.setEmail("newuser@example.com");
        user.setPassword("Pass123!");
        Role role = new Role();
        role.setRoleId(1L);
        user.setRole(role);

        Mockito.when(userRepository.findByUsername("newuser")).thenReturn(null);
        Mockito.when(userRepository.findByEmail("newuser@example.com")).thenReturn(null);
        Mockito.when(userRepository.save(user)).thenReturn(user);
        Mockito.when(roleRepository.findById(1L)).thenReturn(Optional.of(role)); // Line 58

        User registeredUser = userService.registerUser(user);

        Assertions.assertNotNull(registeredUser);
        Assertions.assertEquals("newuser", registeredUser.getUsername());
        Assertions.assertEquals(1L, registeredUser.getRole().getRoleId());
    }

    @Test
    void testGetUserById() {
    	User expectedUser = new User();
        expectedUser.setUserId(1L);
        expectedUser.setUsername("testuser");
        // ... set other properties

        Mockito.when(userRepository.findByUsername("testuser")).thenReturn(expectedUser);

        User actualUser = userService.getUserById(1L); // Or however you are calling it
        Assertions.assertNotNull(actualUser);
        Assertions.assertEquals("testuser", actualUser.getUsername());
    }
}