package com.hexaware.maverickbank.service.interfaces;

import java.util.List;

import com.hexaware.maverickbank.dto.BankEmployeeCreateRequestDTO;
import com.hexaware.maverickbank.dto.BankEmployeeDTO;
import com.hexaware.maverickbank.dto.BankEmployeeUpdateRequestDTO;
import com.hexaware.maverickbank.dto.UserDTO;
import com.hexaware.maverickbank.dto.UserRegistrationRequestDTO;
import com.hexaware.maverickbank.dto.UserUpdateRequestDTO;

public interface AdminService {
	UserDTO createUser(UserRegistrationRequestDTO userCreateRequestDTO);
	UserDTO getUserById(Long userId);
	List<UserDTO> getAllUsers();
	UserDTO updateUser(Long userId, UserUpdateRequestDTO userUpdateRequestDTO);
	void deleteUser(Long userId);

    // Bank Employee Management
    BankEmployeeDTO createBankEmployee(BankEmployeeCreateRequestDTO bankEmployeeCreateRequestDTO);
    BankEmployeeDTO getBankEmployeeById(Long employeeId);
    List<BankEmployeeDTO> getAllBankEmployees();
    BankEmployeeDTO updateBankEmployee(Long employeeId, BankEmployeeUpdateRequestDTO bankEmployeeUpdateRequestDTO);
    void deleteBankEmployee(Long employeeId);
}