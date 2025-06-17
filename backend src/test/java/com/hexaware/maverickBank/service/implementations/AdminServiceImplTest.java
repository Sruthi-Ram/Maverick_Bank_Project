package com.hexaware.maverickBank.service.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.hexaware.maverickBank.dto.BankEmployeeCreateRequestDTO;
import com.hexaware.maverickBank.dto.BankEmployeeDTO;
import com.hexaware.maverickBank.dto.BankEmployeeUpdateRequestDTO;
import com.hexaware.maverickBank.dto.UserDTO;
import com.hexaware.maverickBank.dto.UserRegistrationRequestDTO;
import com.hexaware.maverickBank.dto.UserUpdateRequestDTO;
import com.hexaware.maverickBank.entity.BankBranch;
import com.hexaware.maverickBank.entity.BankEmployee;
import com.hexaware.maverickBank.entity.Role;
import com.hexaware.maverickBank.entity.User;
import com.hexaware.maverickBank.repository.IBankEmployeeRepository;
import com.hexaware.maverickBank.repository.IRoleRepository;
import com.hexaware.maverickBank.repository.IUserRepository;

class AdminServiceImplTest {

    @InjectMocks
    private AdminServiceImpl adminService;

    @Mock
    private IBankEmployeeRepository bankEmployeeRepository;

    @Mock
    private IUserRepository userRepository;

    @Mock
    private IRoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private Role customerRole;
    private User user;
    private UserDTO userDTO;
    private BankEmployee bankEmployee;
    private BankEmployeeDTO bankEmployeeDTO;
    private BankBranch branch;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customerRole = new Role();
        customerRole.setRoleId(1L);
        customerRole.setName("CUSTOMER");

        user = new User();
        user.setUserId(1L);
        user.setUsername("testUser");
        user.setPassword("password");
        user.setEmail("test@example.com");
        user.setRole(customerRole);

        userDTO = new UserDTO();
        userDTO.setUserId(1L);
        userDTO.setUsername("testUser");
        userDTO.setEmail("test@example.com");
        userDTO.setRoleId(1L);

        branch = new BankBranch();
        branch.setBranchId(101L);
        branch.setName("Test Branch");
        branch.setAddress("Test Address");
        branch.setIfscPrefix("TEST");

        bankEmployee = new BankEmployee();
        bankEmployee.setEmployeeId(1L);
        bankEmployee.setUserId(user); // Corrected setter
        bankEmployee.setName("Test Employee");
        bankEmployee.setContactNumber("1234567890");
        bankEmployee.setBranch(branch);

        bankEmployeeDTO = new BankEmployeeDTO();
        bankEmployeeDTO.setEmployeeId(1L);
        bankEmployeeDTO.setUserId(user.getUserId());
        bankEmployeeDTO.setName("Test Employee");
        bankEmployeeDTO.setContactNumber("1234567890");
        bankEmployeeDTO.setBranchId(branch.getBranchId());
    }

    @Test
    void testCreateUser() {
        UserRegistrationRequestDTO requestDTO = new UserRegistrationRequestDTO();
        requestDTO.setUsername("newUser");
        requestDTO.setPassword("newPassword");
        requestDTO.setEmail("new@example.com");
        requestDTO.setRoleId(1L);

        User newUser = new User();
        BeanUtils.copyProperties(requestDTO, newUser);
        newUser.setRole(customerRole);
        newUser.setUserId(2L);
        when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
        when(roleRepository.findById(1L)).thenReturn(Optional.of(customerRole));
        when(userRepository.save(any(User.class))).thenReturn(newUser);

        UserDTO createdUserDTO = adminService.createUser(requestDTO);

        assertNotNull(createdUserDTO);
        assertEquals("newUser", createdUserDTO.getUsername());
        assertEquals("new@example.com", createdUserDTO.getEmail());
        assertEquals(1L, createdUserDTO.getRoleId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testGetUserById_UserFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        UserDTO result = adminService.getUserById(1L);
        assertNotNull(result);
        assertEquals(user.getUserId(), result.getUserId());
    }

    @Test
    void testGetUserById_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        UserDTO result = adminService.getUserById(1L);
        assertNull(result);
    }

    @Test
    void testGetAllUsers() {
        when(userRepository.findAll()).thenReturn(Collections.singletonList(user));
        List<UserDTO> result = adminService.getAllUsers();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(user.getUserId(), result.get(0).getUserId());
    }

    @Test
    void testUpdateUser_UserFound() {
        UserUpdateRequestDTO updateRequestDTO = new UserUpdateRequestDTO();
        updateRequestDTO.setEmail("updated@example.com");
        updateRequestDTO.setPassword("updatedPassword");
        updateRequestDTO.setRoleId(2L);

        Role newRole = new Role();
        newRole.setRoleId(2L);
        newRole.setName("BANK_EMPLOYEE");

        User updatedUser = new User();
        updatedUser.setUserId(1L);
        updatedUser.setUsername("testUser");
        updatedUser.setPassword("encodedUpdatedPassword");
        updatedUser.setEmail("updated@example.com");
        updatedUser.setRole(newRole);

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("updatedPassword")).thenReturn("encodedUpdatedPassword");
        when(roleRepository.findById(2L)).thenReturn(Optional.of(newRole));
        when(userRepository.save(any(User.class))).thenReturn(updatedUser);

        UserDTO result = adminService.updateUser(1L, updateRequestDTO);

        assertNotNull(result);
        assertEquals("updated@example.com", result.getEmail());
        assertEquals(2L, result.getRoleId());
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void testUpdateUser_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        UserUpdateRequestDTO updateRequestDTO = new UserUpdateRequestDTO();
        UserDTO result = adminService.updateUser(1L, updateRequestDTO);
        assertNull(result);
    }

    @Test
    void testDeleteUser() {
        adminService.deleteUser(1L);
        verify(userRepository, times(1)).deleteById(1L);
    }

    @Test
    void testCreateBankEmployee() {
        BankEmployeeCreateRequestDTO requestDTO = new BankEmployeeCreateRequestDTO();
        requestDTO.setUserId(2L);
        requestDTO.setName("New Employee");
        requestDTO.setContactNumber("9876543210");
        requestDTO.setBranchId(101L);

        User newUser = new User();
        newUser.setUserId(2L);
        when(userRepository.findById(2L)).thenReturn(Optional.of(newUser));

        BankEmployee savedBankEmployee = new BankEmployee();
        savedBankEmployee.setEmployeeId(2L); // Or any ID you want for the saved employee
        savedBankEmployee.setUserId(newUser); // Corrected setter
        savedBankEmployee.setName(requestDTO.getName());
        savedBankEmployee.setContactNumber(requestDTO.getContactNumber());
        savedBankEmployee.setBranch(branch); // Assuming 'branch' from setUp() is okay here

        when(bankEmployeeRepository.save(any(BankEmployee.class))).thenReturn(savedBankEmployee);

        BankEmployeeDTO createdEmployeeDTO = adminService.createBankEmployee(requestDTO);

        assertNotNull(createdEmployeeDTO);
        assertEquals(newUser.getUserId(), createdEmployeeDTO.getUserId());
        assertEquals(requestDTO.getName(), createdEmployeeDTO.getName()); // Use the name from the request
        assertEquals(requestDTO.getContactNumber(), createdEmployeeDTO.getContactNumber()); // Use contact number from request
        verify(bankEmployeeRepository, times(1)).save(any(BankEmployee.class));
    }

    @Test
    void testGetBankEmployeeById_EmployeeFound() {
        when(bankEmployeeRepository.findById(1L)).thenReturn(Optional.of(bankEmployee));
        BankEmployeeDTO result = adminService.getBankEmployeeById(1L);
        assertNotNull(result);
        assertEquals(bankEmployee.getEmployeeId(), result.getEmployeeId());
    }

    @Test
    void testGetBankEmployeeById_EmployeeNotFound() {
        when(bankEmployeeRepository.findById(1L)).thenReturn(Optional.empty());
        BankEmployeeDTO result = adminService.getBankEmployeeById(1L);
        assertNull(result);
    }

    @Test
    void testGetAllBankEmployees() {
        when(bankEmployeeRepository.findAll()).thenReturn(Collections.singletonList(bankEmployee));
        List<BankEmployeeDTO> result = adminService.getAllBankEmployees();
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(bankEmployee.getEmployeeId(), result.get(0).getEmployeeId());
    }

    @Test
    void testUpdateBankEmployee_EmployeeFound() {
        BankEmployeeUpdateRequestDTO updateRequestDTO = new BankEmployeeUpdateRequestDTO();
        updateRequestDTO.setName("Updated Employee");
        updateRequestDTO.setContactNumber("0123456789");
        updateRequestDTO.setBranchId(102L);
        BankBranch updatedBranch = new BankBranch();
        updatedBranch.setBranchId(102L);
        updatedBranch.setName("Updated Branch");

        BankEmployee updatedEmployee = new BankEmployee();
        updatedEmployee.setEmployeeId(1L);
        updatedEmployee.setUserId(user); // Corrected setter
        updatedEmployee.setName("Updated Employee");
        updatedEmployee.setContactNumber("0123456789");
        updatedEmployee.setBranch(updatedBranch);

        when(bankEmployeeRepository.findById(1L)).thenReturn(Optional.of(bankEmployee));
        when(bankEmployeeRepository.save(any(BankEmployee.class))).thenReturn(updatedEmployee);

        BankEmployeeDTO result = adminService.updateBankEmployee(1L, updateRequestDTO);

        assertNotNull(result);
        assertEquals("Updated Employee", result.getName());
        assertEquals("0123456789", result.getContactNumber());
        assertEquals(102L, result.getBranchId());
        verify(bankEmployeeRepository, times(1)).save(any(BankEmployee.class));
    }

    @Test
    void testUpdateBankEmployee_EmployeeNotFound() {
        when(bankEmployeeRepository.findById(1L)).thenReturn(Optional.empty());
        BankEmployeeUpdateRequestDTO updateRequestDTO = new BankEmployeeUpdateRequestDTO();
        BankEmployeeDTO result = adminService.updateBankEmployee(1L, updateRequestDTO);
        assertNull(result);
    }

    @Test
    void testDeleteBankEmployee() {
        adminService.deleteBankEmployee(1L);
        verify(bankEmployeeRepository, times(1)).deleteById(1L);
    }
}