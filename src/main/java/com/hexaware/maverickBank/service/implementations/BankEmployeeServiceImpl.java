package com.hexaware.maverickBank.service.implementations;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexaware.maverickBank.entity.BankEmployee;
import com.hexaware.maverickBank.repository.IBankEmployeeRepository;
import com.hexaware.maverickBank.service.interfaces.BankBranchService;
import com.hexaware.maverickBank.service.interfaces.BankEmployeeService;
import com.hexaware.maverickBank.service.interfaces.UserService;

import jakarta.validation.ValidationException;

@Service
public class BankEmployeeServiceImpl implements BankEmployeeService {

    @Autowired
    private IBankEmployeeRepository bankEmployeeRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BankBranchService bankBranchService;

    private void validateBankEmployee(BankEmployee employee) {
        if (employee.getName() == null || employee.getName().isEmpty()) {
            throw new ValidationException("Employee name cannot be empty");
        }
        if (employee.getContactNumber() == null || employee.getContactNumber().isEmpty()) {
            throw new ValidationException("Employee contact number cannot be empty");
        }
        if (employee.getBranch() == null || employee.getBranch().getBranchId() == null) {
            throw new ValidationException("Branch ID is required");
        }
        bankBranchService.getBankBranchById(employee.getBranch().getBranchId()); // Ensure branch exists
    }

    @Override
    public BankEmployee createBankEmployee(BankEmployee bankEmployee) {
        if (bankEmployee.getUser() == null || bankEmployee.getUser().getUserId() == null || userService.getUserById(bankEmployee.getUser().getUserId()) == null) {
            throw new ValidationException("User ID is required and must exist");
        }
        validateBankEmployee(bankEmployee);
        return bankEmployeeRepository.save(bankEmployee);
    }

    @Override
    public BankEmployee getBankEmployeeById(Long employeeId) {
        return bankEmployeeRepository.findById(employeeId)
                .orElseThrow(() -> new NoSuchElementException("Bank employee not found with ID: " + employeeId));
    }

    @Override
    public List<BankEmployee> getAllBankEmployees() {
        return bankEmployeeRepository.findAll();
    }

    @Override
    public BankEmployee updateBankEmployee(Long employeeId, BankEmployee bankEmployee) {
        BankEmployee existingEmployee = bankEmployeeRepository.findById(employeeId)
                .orElseThrow(() -> new NoSuchElementException("Bank employee not found with ID: " + employeeId));
        bankEmployee.setEmployeeId(employeeId);
        validateBankEmployee(bankEmployee);
        return bankEmployeeRepository.save(bankEmployee);
    }

    @Override
    public void deleteBankEmployee(Long employeeId) {
        if (!bankEmployeeRepository.existsById(employeeId)) {
            throw new NoSuchElementException("Bank employee not found with ID: " + employeeId);
        }
        bankEmployeeRepository.deleteById(employeeId);
    }

    @Override
    public BankEmployee getBankEmployeeByUserId(Long userId) {
        BankEmployee bankEmployee = bankEmployeeRepository.findByUser_UserId(userId);
        if (bankEmployee == null) {
            throw new NoSuchElementException("Bank employee not found for User ID: " + userId);
        }
        return bankEmployee;
    }
}