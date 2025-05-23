package com.hexaware.maverickBank.service.interfaces;

import com.hexaware.maverickBank.entity.BankEmployee;
import java.util.List;

public interface BankEmployeeService {
    BankEmployee createBankEmployee(BankEmployee bankEmployee);
    BankEmployee getBankEmployeeById(Long employeeId);
    List<BankEmployee> getAllBankEmployees();
    BankEmployee updateBankEmployee(Long employeeId, BankEmployee bankEmployee);
    void deleteBankEmployee(Long employeeId);
    BankEmployee getBankEmployeeByUserId(Long userId);
}