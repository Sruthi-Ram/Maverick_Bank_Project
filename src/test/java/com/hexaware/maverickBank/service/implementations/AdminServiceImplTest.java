package com.hexaware.maverickBank.service.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hexaware.maverickBank.dto.BankEmployeeCreateRequestDTO;
import com.hexaware.maverickBank.dto.BankEmployeeDTO;
import com.hexaware.maverickBank.dto.BankEmployeeUpdateRequestDTO;
import com.hexaware.maverickBank.dto.UserDTO;
import com.hexaware.maverickBank.dto.UserRegistrationRequestDTO;
import com.hexaware.maverickBank.dto.UserUpdateRequestDTO;
import com.hexaware.maverickBank.entity.BankEmployee;
import com.hexaware.maverickBank.entity.Role;
import com.hexaware.maverickBank.entity.User;
import com.hexaware.maverickBank.repository.IBankEmployeeRepository;
import com.hexaware.maverickBank.repository.IRoleRepository;
import com.hexaware.maverickBank.repository.IUserRepository;

class AdminServiceImplTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IBankEmployeeRepository bankEmployeeRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private IRoleRepository roleRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
    }

    @Test
    void testCreateUser() {
        UserRegistrationRequestDTO requestDTO = new UserRegistrationRequestDTO();
        requestDTO.setUsername("testUser");
        requestDTO.setPassword("password");
        User savedUser = new User();
        savedUser.setUserId(1L);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        UserDTO userDTO = adminService.createUser(requestDTO);
        assertNotNull(userDTO);
        assertEquals(1L, userDTO.getUserId());
    }

    @Test
    void testGetUserById_Found() {
        User user = new User();
        user.setUserId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        UserDTO userDTO = adminService.getUserById(1L);
        assertNotNull(userDTO);
        assertEquals(1L, userDTO.getUserId());
    }

    @Test
    void testGetUserById_NotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertNull(adminService.getUserById(1L));
    }

    @Test
    void testGetAllUsers() {
        List<User> users = new ArrayList<>();
        users.add(new User());
        users.add(new User());
        when(userRepository.findAll()).thenReturn(users);

        List<UserDTO> userDTOs = adminService.getAllUsers();
        assertNotNull(userDTOs);
        assertEquals(2, userDTOs.size());
    }

    @Test
    void testUpdateUser_Found() {
        UserUpdateRequestDTO requestDTO = new UserUpdateRequestDTO();
        requestDTO.setEmail("new@example.com");
        requestDTO.setPassword("newPassword");
        requestDTO.setRoleId(2L);
        User existingUser = new User();
        existingUser.setUserId(1L);
        Role role = new Role();
        role.setRoleId(2L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        when(roleRepository.findById(2L)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(existingUser);

        UserDTO userDTO = adminService.updateUser(1L, requestDTO);
        assertNotNull(userDTO);
        assertEquals(1L, userDTO.getUserId());
    }

    @Test
    void testUpdateUser_NotFound() {
        UserUpdateRequestDTO requestDTO = new UserUpdateRequestDTO();
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        assertNull(adminService.updateUser(1L, requestDTO));
    }

    @Test
    void testDeleteUser() {
        adminService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testCreateBankEmployee() {
        BankEmployeeCreateRequestDTO requestDTO = new BankEmployeeCreateRequestDTO();
        requestDTO.setUserId(1L);
        requestDTO.setEmployeeId("E123");
        BankEmployee savedEmployee = new BankEmployee();
        savedEmployee.setEmployeeId(1L);
        when(bankEmployeeRepository.save(any(BankEmployee.class))).thenReturn(savedEmployee);

        BankEmployeeDTO employeeDTO = adminService.createBankEmployee(requestDTO);
        assertNotNull(employeeDTO);
        assertEquals(1L, employeeDTO.getEmployeeId());
    }

    @Test
    void testGetBankEmployeeById_Found() {
        BankEmployee employee = new BankEmployee();
        employee.setEmployeeId(1L);
        when(bankEmployeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        BankEmployeeDTO employeeDTO = adminService.getBankEmployeeById(1L);
        assertNotNull(employeeDTO);
        assertEquals(1L, employeeDTO.getEmployeeId());
    }

    @Test
    void testGetBankEmployeeById_NotFound() {
        when(bankEmployeeRepository.findById(1L)).thenReturn(Optional.empty());
        assertNull(adminService.getBankEmployeeById(1L));
    }

    @Test
    void testGetAllBankEmployees() {
        List<BankEmployee> employees = new ArrayList<>();
        employees.add(new BankEmployee());
        employees.add(new BankEmployee());
        when(bankEmployeeRepository.findAll()).thenReturn(employees);

        List<BankEmployeeDTO> employeeDTOs = adminService.getAllBankEmployees();
        assertNotNull(employeeDTOs);
        assertEquals(2, employeeDTOs.size());
    }

    @Test
    void testUpdateBankEmployee_Found() {
        BankEmployeeUpdateRequestDTO requestDTO = new BankEmployeeUpdateRequestDTO();
        requestDTO.setContactNumber("1234567890");
        BankEmployee existingEmployee = new BankEmployee();
        existingEmployee.setEmployeeId(1L);
        when(bankEmployeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));
        when(bankEmployeeRepository.save(any(BankEmployee.class))).thenReturn(existingEmployee);

        BankEmployeeDTO employeeDTO = adminService.updateBankEmployee(1L, requestDTO);
        assertNotNull(employeeDTO);
        assertEquals(1L, employeeDTO.getEmployeeId());
    }

    @Test
    void testUpdateBankEmployee_NotFound() {
        BankEmployeeUpdateRequestDTO requestDTO = new BankEmployeeUpdateRequestDTO();
        when(bankEmployeeRepository.findById(1L)).thenReturn(Optional.empty());
        assertNull(adminService.updateBankEmployee(1L, requestDTO));
    }

    @Test
    void testDeleteBankEmployee() {
        adminService.deleteBankEmployee(1L);
        verify(bankEmployeeRepository, times(1)).deleteById(1L);
    }
}