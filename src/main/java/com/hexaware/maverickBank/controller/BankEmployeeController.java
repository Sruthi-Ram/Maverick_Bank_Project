package com.hexaware.maverickBank.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hexaware.maverickBank.dto.BankEmployeeCreateRequestDTO;
import com.hexaware.maverickBank.dto.BankEmployeeDTO;
import com.hexaware.maverickBank.dto.BankEmployeeUpdateRequestDTO;
import com.hexaware.maverickBank.service.interfaces.BankEmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/employees")
@PreAuthorize("hasRole('BANK_EMPLOYEE')")
public class BankEmployeeController {

    @Autowired
    private BankEmployeeService bankEmployeeService;

    @PostMapping("/createBankEmployee")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<BankEmployeeDTO> createBankEmployee(@Valid @RequestBody BankEmployeeCreateRequestDTO bankEmployeeCreateRequestDTO) {
        BankEmployeeDTO createdBankEmployee = bankEmployeeService.createBankEmployee(bankEmployeeCreateRequestDTO);
        return new ResponseEntity<>(createdBankEmployee, HttpStatus.CREATED);
    }

    @GetMapping("/getBankEmployeeById/{employeeId}")
    public ResponseEntity<BankEmployeeDTO> getBankEmployeeById(@PathVariable Long employeeId) {
        try {
            BankEmployeeDTO bankEmployeeDTO = bankEmployeeService.getBankEmployeeById(employeeId);
            return new ResponseEntity<>(bankEmployeeDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAllBankEmployees")
    public ResponseEntity<List<BankEmployeeDTO>> getAllBankEmployees() {
        List<BankEmployeeDTO> bankEmployeeDTOList = bankEmployeeService.getAllBankEmployees();
        return new ResponseEntity<>(bankEmployeeDTOList, HttpStatus.OK);
    }

    @PutMapping("/updateBankEmployee/{employeeId}")
    public ResponseEntity<BankEmployeeDTO> updateBankEmployee(@PathVariable Long employeeId, @Valid @RequestBody BankEmployeeUpdateRequestDTO bankEmployeeUpdateRequestDTO) {
        try {
            BankEmployeeDTO updatedBankEmployee = bankEmployeeService.updateBankEmployee(employeeId, bankEmployeeUpdateRequestDTO);
            return new ResponseEntity<>(updatedBankEmployee, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteBankEmployee/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteBankEmployee(@PathVariable Long employeeId) {
        try {
            bankEmployeeService.deleteBankEmployee(employeeId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getBankEmployeeByUserId/{userId}")
    public ResponseEntity<BankEmployeeDTO> getBankEmployeeByUserId(@PathVariable Long userId) {
        try {
            BankEmployeeDTO bankEmployeeDTO = bankEmployeeService.getBankEmployeeByUserId(userId);
            return new ResponseEntity<>(bankEmployeeDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}