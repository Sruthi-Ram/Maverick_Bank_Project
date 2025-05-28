package com.hexaware.maverickBank.service.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hexaware.maverickBank.dto.BankBranchDTO;
import com.hexaware.maverickBank.dto.BankEmployeeCreateRequestDTO;
import com.hexaware.maverickBank.dto.BankEmployeeDTO;
import com.hexaware.maverickBank.dto.BankEmployeeUpdateRequestDTO;
import com.hexaware.maverickBank.dto.UserDTO;
import com.hexaware.maverickBank.entity.BankBranch;
import com.hexaware.maverickBank.entity.BankEmployee;
import com.hexaware.maverickBank.entity.User;
import com.hexaware.maverickBank.repository.IBankEmployeeRepository;
import com.hexaware.maverickBank.service.interfaces.BankBranchService;
import com.hexaware.maverickBank.service.interfaces.UserService;

import jakarta.validation.ValidationException;

class BankEmployeeServiceImplTest {

    @Mock
    private IBankEmployeeRepository bankEmployeeRepository;

    @Mock
    private UserService userService;

    @Mock
    private BankBranchService bankBranchService;

    @InjectMocks
    private BankEmployeeServiceImpl bankEmployeeService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBankEmployee_Success() {
        BankEmployeeCreateRequestDTO requestDTO = new BankEmployeeCreateRequestDTO(1L, "John Doe", "123-456-7890", 101L);
        User user = new User();
        user.setUserId(1L);
        BankBranch branch = new BankBranch();
        branch.setBranchId(101L);
        BankEmployee savedEmployee = new BankEmployee();
        savedEmployee.setEmployeeId(1L);
        when(userService.getUserById(1L)).thenReturn(new UserDTO());
        when(bankBranchService.getBankBranchById(101L)).thenReturn(new BankBranchDTO());
        when(bankEmployeeRepository.save(any(BankEmployee.class))).thenReturn(savedEmployee);

        BankEmployeeDTO employeeDTO = bankEmployeeService.createBankEmployee(requestDTO);
        assertNotNull(employeeDTO);
        assertEquals(1L, employeeDTO.getEmployeeId());
    }

    @Test
    void testCreateBankEmployee_ValidationException_NullUserId() {
        BankEmployeeCreateRequestDTO requestDTO = new BankEmployeeCreateRequestDTO(null, "John Doe", "123-456-7890", 101L);
        assertThrows(ValidationException.class, () -> bankEmployeeService.createBankEmployee(requestDTO));
    }

    @Test
    void testCreateBankEmployee_ValidationException_UserNotFound() {
        BankEmployeeCreateRequestDTO requestDTO = new BankEmployeeCreateRequestDTO(1L, "John Doe", "123-456-7890", 101L);
        when(userService.getUserById(1L)).thenReturn(null);
        assertThrows(ValidationException.class, () -> bankEmployeeService.createBankEmployee(requestDTO));
    }

    @Test
    void testCreateBankEmployee_ValidationException_EmptyName() {
        BankEmployeeCreateRequestDTO requestDTO = new BankEmployeeCreateRequestDTO(1L, "", "123-456-7890", 101L);
        when(userService.getUserById(1L)).thenReturn(new UserDTO());
        when(bankBranchService.getBankBranchById(101L)).thenReturn(new BankBranchDTO());
        assertThrows(ValidationException.class, () -> bankEmployeeService.createBankEmployee(requestDTO));
    }

    @Test
    void testCreateBankEmployee_ValidationException_EmptyContactNumber() {
        BankEmployeeCreateRequestDTO requestDTO = new BankEmployeeCreateRequestDTO(1L, "John Doe", "", 101L);
        when(userService.getUserById(1L)).thenReturn(new UserDTO());
        when(bankBranchService.getBankBranchById(101L)).thenReturn(new BankBranchDTO());
        assertThrows(ValidationException.class, () -> bankEmployeeService.createBankEmployee(requestDTO));
    }

    @Test
    void testCreateBankEmployee_ValidationException_NullBranchId() {
        BankEmployeeCreateRequestDTO requestDTO = new BankEmployeeCreateRequestDTO(1L, "John Doe", "123-456-7890", null);
        when(userService.getUserById(1L)).thenReturn(new UserDTO());
        assertThrows(ValidationException.class, () -> bankEmployeeService.createBankEmployee(requestDTO));
    }

    @Test
    void testCreateBankEmployee_ValidationException_BranchNotFound() {
        BankEmployeeCreateRequestDTO requestDTO = new BankEmployeeCreateRequestDTO(1L, "John Doe", "123-456-7890", 101L);
        when(userService.getUserById(1L)).thenReturn(new UserDTO());
        when(bankBranchService.getBankBranchById(101L)).thenThrow(new NoSuchElementException("Bank branch not found with ID: 101"));
        assertThrows(ValidationException.class, () -> bankEmployeeService.createBankEmployee(requestDTO));
    }

    @Test
    void testGetBankEmployeeById_Found() {
        BankEmployee employee = new BankEmployee();
        employee.setEmployeeId(1L);
        when(bankEmployeeRepository.findById(1L)).thenReturn(Optional.of(employee));

        BankEmployeeDTO employeeDTO = bankEmployeeService.getBankEmployeeById(1L);
        assertNotNull(employeeDTO);
        assertEquals(1L, employeeDTO.getEmployeeId());
    }

    @Test
    void testGetBankEmployeeById_NotFound() {
        when(bankEmployeeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> bankEmployeeService.getBankEmployeeById(1L));
    }

    @Test
    void testGetAllBankEmployees() {
        List<BankEmployee> employees = new ArrayList<>();
        employees.add(new BankEmployee());
        employees.add(new BankEmployee());
        when(bankEmployeeRepository.findAll()).thenReturn(employees);

        List<BankEmployeeDTO> employeeDTOs = bankEmployeeService.getAllBankEmployees();
        assertNotNull(employeeDTOs);
        assertEquals(2, employeeDTOs.size());
    }

    @Test
    void testUpdateBankEmployee_Success() {
        BankEmployeeUpdateRequestDTO requestDTO = new BankEmployeeUpdateRequestDTO();
        requestDTO.setName("Updated Name");
        requestDTO.setContactNumber("987-654-3210");
        requestDTO.setBranchId(202L);
        BankEmployee existingEmployee = new BankEmployee();
        existingEmployee.setEmployeeId(1L);
        when(bankEmployeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));
        when(bankBranchService.getBankBranchById(202L)).thenReturn(new BankBranchDTO());
        when(bankEmployeeRepository.save(any(BankEmployee.class))).thenReturn(existingEmployee);

        BankEmployeeDTO employeeDTO = bankEmployeeService.updateBankEmployee(1L, requestDTO);
        assertNotNull(employeeDTO);
        assertEquals(1L, employeeDTO.getEmployeeId());
    }

    @Test
    void testUpdateBankEmployee_NotFound() {
        BankEmployeeUpdateRequestDTO requestDTO = new BankEmployeeUpdateRequestDTO();
        when(bankEmployeeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> bankEmployeeService.updateBankEmployee(1L, requestDTO));
    }

    @Test
    void testUpdateBankEmployee_BranchNotFound() {
        BankEmployeeUpdateRequestDTO requestDTO = new BankEmployeeUpdateRequestDTO();
        requestDTO.setBranchId(202L);
        BankEmployee existingEmployee = new BankEmployee();
        existingEmployee.setEmployeeId(1L);
        when(bankEmployeeRepository.findById(1L)).thenReturn(Optional.of(existingEmployee));
        when(bankBranchService.getBankBranchById(202L)).thenThrow(new NoSuchElementException("Bank branch not found with ID: 202"));
        assertThrows(ValidationException.class, () -> bankEmployeeService.updateBankEmployee(1L, requestDTO));
    }

    @Test
    void testDeleteBankEmployee_Success() {
        when(bankEmployeeRepository.existsById(1L)).thenReturn(true);
        bankEmployeeService.deleteBankEmployee(1L);
        verify(bankEmployeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBankEmployee_NotFound() {
        when(bankEmployeeRepository.existsById(1L)).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> bankEmployeeService.deleteBankEmployee(1L));
        verify(bankEmployeeRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetBankEmployeeByUserId_Success() {
        BankEmployee employee = new BankEmployee();
        employee.setEmployeeId(1L);
        User user = new User();
        user.setUserId(123L);
        employee.setUser(user);
        when(bankEmployeeRepository.findByUser_UserId(123L)).thenReturn(employee);

        BankEmployeeDTO employeeDTO = bankEmployeeService.getBankEmployeeByUserId(123L);
        assertNotNull(employeeDTO);
        assertEquals(1L, employeeDTO.getEmployeeId());
        assertEquals(123L, employeeDTO.getUserId());
    }

    @Test
    void testGetBankEmployeeByUserId_NotFound() {
        when(bankEmployeeRepository.findByUser_UserId(123L)).thenReturn(null);
        assertThrows(NoSuchElementException.class, () -> bankEmployeeService.getBankEmployeeByUserId(123L));
    }
}