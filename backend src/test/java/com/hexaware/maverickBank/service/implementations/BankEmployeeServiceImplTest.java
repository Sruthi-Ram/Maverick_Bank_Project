package com.hexaware.maverickbank.service.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;

import com.hexaware.maverickbank.dto.BankBranchDTO;
import com.hexaware.maverickbank.dto.BankEmployeeCreateRequestDTO;
import com.hexaware.maverickbank.dto.BankEmployeeDTO;
import com.hexaware.maverickbank.dto.BankEmployeeUpdateRequestDTO;
import com.hexaware.maverickbank.dto.UserDTO;
import com.hexaware.maverickbank.dto.entity.BankBranch;
import com.hexaware.maverickbank.dto.entity.BankEmployee;
import com.hexaware.maverickbank.dto.entity.User;
import com.hexaware.maverickbank.repository.IBankEmployeeRepository;
import com.hexaware.maverickbank.service.interfaces.BankBranchService;
import com.hexaware.maverickbank.service.interfaces.UserService;

import jakarta.validation.ValidationException;

class BankEmployeeServiceImplTest {

    @InjectMocks
    private BankEmployeeServiceImpl bankEmployeeService;

    @Mock
    private IBankEmployeeRepository bankEmployeeRepository;

    @Mock
    private UserService userService;

    @Mock
    private BankBranchService bankBranchService;

    private BankEmployee employee;
    private BankEmployeeDTO employeeDTO;
    private BankEmployeeCreateRequestDTO createRequestDTO;
    private BankEmployeeUpdateRequestDTO updateRequestDTO;
    private User user;
    private UserDTO userDTOForService; // Added UserDTO for mocking UserService
    private BankBranch branch;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUserId(1L);
        branch = new BankBranch();
        branch.setBranchId(101L);
        employee = new BankEmployee();
        employee.setEmployeeId(1L);
        employee.setUserId(user);
        employee.setName("Test Employee");
        employee.setContactNumber("1234567890");
        employee.setBranch(branch);
        employeeDTO = new BankEmployeeDTO();
        employeeDTO.setEmployeeId(1L);
        employeeDTO.setUserId(1L);
        employeeDTO.setName("Test Employee");
        employeeDTO.setContactNumber("1234567890");
        employeeDTO.setBranchId(101L);
        createRequestDTO = new BankEmployeeCreateRequestDTO();
        createRequestDTO.setUserId(1L);
        createRequestDTO.setName("Test Employee");
        createRequestDTO.setContactNumber("1234567890");
        createRequestDTO.setBranchId(101L);
        updateRequestDTO = new BankEmployeeUpdateRequestDTO();
        userDTOForService = new UserDTO(); // Initialize UserDTO
        userDTOForService.setUserId(1L);
    }

    @Test
    void testCreateBankEmployee() {
        when(userService.getUserById(1L)).thenReturn(userDTOForService); // Use UserDTO here
        when(bankBranchService.getBankBranchById(101L)).thenReturn(new BankBranchDTO());
        when(bankEmployeeRepository.save(any(BankEmployee.class))).thenReturn(employee);

        BankEmployeeDTO createdEmployeeDTO = bankEmployeeService.createBankEmployee(createRequestDTO);
        assertNotNull(createdEmployeeDTO);
        assertEquals(employeeDTO.getEmployeeId(), createdEmployeeDTO.getEmployeeId());
        verify(bankEmployeeRepository, times(1)).save(any(BankEmployee.class));
    }

    @Test
    void testCreateBankEmployee_InvalidUserId() {
        createRequestDTO.setUserId(null);
        assertThrows(ValidationException.class, () -> bankEmployeeService.createBankEmployee(createRequestDTO));
        verify(bankEmployeeRepository, never()).save(any());
    }

    @Test
    void testCreateBankEmployee_NonExistingUserId() {
        createRequestDTO.setUserId(2L);
        when(userService.getUserById(2L)).thenReturn(null);
        assertThrows(ValidationException.class, () -> bankEmployeeService.createBankEmployee(createRequestDTO));
        verify(bankEmployeeRepository, never()).save(any());
    }

    @Test
    void testCreateBankEmployee_EmptyName() {
        createRequestDTO.setName("");
        when(userService.getUserById(1L)).thenReturn(userDTOForService);
        when(bankBranchService.getBankBranchById(101L)).thenReturn(new BankBranchDTO());
        assertThrows(ValidationException.class, () -> bankEmployeeService.createBankEmployee(createRequestDTO));
        verify(bankEmployeeRepository, never()).save(any());
    }

    @Test
    void testGetBankEmployeeById() {
        when(bankEmployeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        BankEmployeeDTO foundEmployeeDTO = bankEmployeeService.getBankEmployeeById(1L);
        assertNotNull(foundEmployeeDTO);
        assertEquals(employeeDTO.getEmployeeId(), foundEmployeeDTO.getEmployeeId());
    }

    @Test
    void testGetBankEmployeeById_NotFound() {
        when(bankEmployeeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> bankEmployeeService.getBankEmployeeById(1L));
    }

    @Test
    void testGetAllBankEmployees() {
        when(bankEmployeeRepository.findAll()).thenReturn(Collections.singletonList(employee));
        List<BankEmployeeDTO> allEmployeesDTO = bankEmployeeService.getAllBankEmployees();
        assertNotNull(allEmployeesDTO);
        assertEquals(1, allEmployeesDTO.size());
        assertEquals(employeeDTO.getEmployeeId(), allEmployeesDTO.get(0).getEmployeeId());
    }

    @Test
    void testUpdateBankEmployee() {
        updateRequestDTO.setName("Updated Employee");
        updateRequestDTO.setBranchId(102L);
        BankBranch updatedBranch = new BankBranch();
        updatedBranch.setBranchId(102L);
        when(bankEmployeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(bankBranchService.getBankBranchById(102L)).thenReturn(new BankBranchDTO());
        BankEmployee updatedEmployee = new BankEmployee();
        BeanUtils.copyProperties(employee, updatedEmployee);
        updatedEmployee.setName("Updated Employee");
        updatedEmployee.setBranch(updatedBranch);
        when(bankEmployeeRepository.save(any(BankEmployee.class))).thenReturn(updatedEmployee);

        BankEmployeeDTO updatedEmployeeDTO = bankEmployeeService.updateBankEmployee(1L, updateRequestDTO);
        assertNotNull(updatedEmployeeDTO);
        assertEquals("Updated Employee", updatedEmployeeDTO.getName());
        assertEquals(102L, updatedEmployeeDTO.getBranchId());
        verify(bankEmployeeRepository, times(1)).save(any(BankEmployee.class));
    }

    @Test
    void testUpdateBankEmployee_NotFound() {
        when(bankEmployeeRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> bankEmployeeService.updateBankEmployee(1L, updateRequestDTO));
    }

    @Test
    void testUpdateBankEmployee_InvalidBranchId() {
        updateRequestDTO.setBranchId(200L);
        when(bankEmployeeRepository.findById(1L)).thenReturn(Optional.of(employee));
        when(bankBranchService.getBankBranchById(200L)).thenThrow(NoSuchElementException.class);
        assertThrows(ValidationException.class, () -> bankEmployeeService.updateBankEmployee(1L, updateRequestDTO));
        verify(bankEmployeeRepository, never()).save(any());
    }

    @Test
    void testDeleteBankEmployee() {
        when(bankEmployeeRepository.existsById(1L)).thenReturn(true);
        bankEmployeeService.deleteBankEmployee(1L);
        verify(bankEmployeeRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBankEmployee_NotFound() {
        when(bankEmployeeRepository.existsById(1L)).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> bankEmployeeService.deleteBankEmployee(1L));
    }

    @Test
    void testGetBankEmployeeByUserId() {
        when(bankEmployeeRepository.findByUser_UserId(1L)).thenReturn(employee);
        BankEmployeeDTO foundEmployeeDTO = bankEmployeeService.getBankEmployeeByUserId(1L);
        assertNotNull(foundEmployeeDTO);
        assertEquals(employeeDTO.getEmployeeId(), foundEmployeeDTO.getEmployeeId());
    }

    @Test
    void testGetBankEmployeeByUserId_NotFound() {
        when(bankEmployeeRepository.findByUser_UserId(1L)).thenReturn(null);
        assertThrows(NoSuchElementException.class, () -> bankEmployeeService.getBankEmployeeByUserId(1L));
    }
}