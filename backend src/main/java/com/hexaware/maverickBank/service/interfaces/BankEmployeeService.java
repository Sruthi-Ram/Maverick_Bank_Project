package com.hexaware.maverickbank.service.interfaces;

import java.util.List;

import com.hexaware.maverickbank.dto.BankEmployeeCreateRequestDTO;
import com.hexaware.maverickbank.dto.BankEmployeeDTO;
import com.hexaware.maverickbank.dto.BankEmployeeUpdateRequestDTO;

public interface BankEmployeeService {
    BankEmployeeDTO createBankEmployee(BankEmployeeCreateRequestDTO bankEmployeeCreateRequestDTO);
    BankEmployeeDTO getBankEmployeeById(Long employeeId);
    List<BankEmployeeDTO> getAllBankEmployees();
    BankEmployeeDTO updateBankEmployee(Long employeeId, BankEmployeeUpdateRequestDTO bankEmployeeUpdateRequestDTO);
    void deleteBankEmployee(Long employeeId);
    BankEmployeeDTO getBankEmployeeByUserId(Long userId);
   
}