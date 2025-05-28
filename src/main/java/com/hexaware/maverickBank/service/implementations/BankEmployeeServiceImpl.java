package com.hexaware.maverickBank.service.implementations;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexaware.maverickBank.dto.BankEmployeeCreateRequestDTO;
import com.hexaware.maverickBank.dto.BankEmployeeDTO;
import com.hexaware.maverickBank.dto.BankEmployeeUpdateRequestDTO;
import com.hexaware.maverickBank.entity.BankBranch;
import com.hexaware.maverickBank.entity.BankEmployee;
import com.hexaware.maverickBank.entity.User;
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
        try {
            bankBranchService.getBankBranchById(employee.getBranch().getBranchId());
        } catch (NoSuchElementException e) {
            throw new ValidationException("Branch with ID " + employee.getBranch().getBranchId() + " not found.");
        }
    }

    @Override
    public BankEmployeeDTO createBankEmployee(BankEmployeeCreateRequestDTO bankEmployeeCreateRequestDTO) {
        if (bankEmployeeCreateRequestDTO.getUserId() == null || userService.getUserById(bankEmployeeCreateRequestDTO.getUserId()) == null) {
            throw new ValidationException("User ID is required and must exist");
        }
        BankEmployee bankEmployee = convertCreateRequestDTOtoEntity(bankEmployeeCreateRequestDTO);
        validateBankEmployee(bankEmployee);
        BankEmployee savedBankEmployee = bankEmployeeRepository.save(bankEmployee);
        return convertEntityToDTO(savedBankEmployee);
    }

    @Override
    public BankEmployeeDTO getBankEmployeeById(Long employeeId) {
        BankEmployee bankEmployee = bankEmployeeRepository.findById(employeeId).orElse(null);
        if (bankEmployee == null) {
            throw new NoSuchElementException("Bank employee not found with ID: " + employeeId);
        }
        return convertEntityToDTO(bankEmployee);
    }

    @Override
    public List<BankEmployeeDTO> getAllBankEmployees() {
        List<BankEmployee> employees = bankEmployeeRepository.findAll();
        List<BankEmployeeDTO> dtos = new ArrayList<BankEmployeeDTO>();
        for (BankEmployee employee : employees) {
            dtos.add(convertEntityToDTO(employee));
        }
        return dtos;
    }

    @Override
    public BankEmployeeDTO updateBankEmployee(Long employeeId, BankEmployeeUpdateRequestDTO bankEmployeeUpdateRequestDTO) {
        BankEmployee existingEmployee = bankEmployeeRepository.findById(employeeId).orElse(null);
        if (existingEmployee == null) {
            throw new NoSuchElementException("Bank employee not found with ID: " + employeeId);
        }
        if (bankEmployeeUpdateRequestDTO.getName() != null) {
            existingEmployee.setName(bankEmployeeUpdateRequestDTO.getName());
        }
        if (bankEmployeeUpdateRequestDTO.getContactNumber() != null) {
            existingEmployee.setContactNumber(bankEmployeeUpdateRequestDTO.getContactNumber());
        }
        if (bankEmployeeUpdateRequestDTO.getBranchId() != null) {
            BankBranch branch = new BankBranch();
            branch.setBranchId(bankEmployeeUpdateRequestDTO.getBranchId());
            existingEmployee.setBranch(branch);
            try {
                bankBranchService.getBankBranchById(bankEmployeeUpdateRequestDTO.getBranchId());
            } catch (NoSuchElementException e) {
                throw new ValidationException("Branch with ID " + bankEmployeeUpdateRequestDTO.getBranchId() + " not found.");
            }
        }
        validateBankEmployee(existingEmployee);
        BankEmployee updatedEmployee = bankEmployeeRepository.save(existingEmployee);
        return convertEntityToDTO(updatedEmployee);
    }

    @Override
    public void deleteBankEmployee(Long employeeId) {
        if (!bankEmployeeRepository.existsById(employeeId)) {
            throw new NoSuchElementException("Bank employee not found with ID: " + employeeId);
        }
        bankEmployeeRepository.deleteById(employeeId);
    }

    @Override
    public BankEmployeeDTO getBankEmployeeByUserId(Long userId) {
        BankEmployee bankEmployee = bankEmployeeRepository.findByUser_UserId(userId);
        if (bankEmployee == null) {
            throw new NoSuchElementException("Bank employee not found for User ID: " + userId);
        }
        return convertEntityToDTO(bankEmployee);
    }

    private BankEmployeeDTO convertEntityToDTO(BankEmployee bankEmployee) {
        BankEmployeeDTO dto = new BankEmployeeDTO();
        dto.setEmployeeId(bankEmployee.getEmployeeId());
        if (bankEmployee.getUser() != null) {
            dto.setUserId(bankEmployee.getUser().getUserId());
        }
        dto.setName(bankEmployee.getName());
        dto.setContactNumber(bankEmployee.getContactNumber());
        if (bankEmployee.getBranch() != null) {
            dto.setBranchId(bankEmployee.getBranch().getBranchId());
        }
        return dto;
    }

    private BankEmployee convertCreateRequestDTOtoEntity(BankEmployeeCreateRequestDTO createRequestDTO) {
        BankEmployee bankEmployee = new BankEmployee();
        User user = new User();
        user.setUserId(createRequestDTO.getUserId());
        bankEmployee.setUser(user);
        bankEmployee.setName(createRequestDTO.getName());
        bankEmployee.setContactNumber(createRequestDTO.getContactNumber());
        BankBranch branch = new BankBranch();
        branch.setBranchId(createRequestDTO.getBranchId());
        bankEmployee.setBranch(branch);
        return bankEmployee;
    }
}
