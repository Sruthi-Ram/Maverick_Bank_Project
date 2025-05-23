package com.hexaware.maverickBank.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.hexaware.maverickBank.entity.BankEmployee;
import com.hexaware.maverickBank.service.interfaces.BankEmployeeService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/employees")
public class BankEmployeeController {

    @Autowired
    private BankEmployeeService bankEmployeeService;

    @PostMapping("/createBankEmployee")
    @ResponseStatus(HttpStatus.CREATED)
    public BankEmployee createBankEmployee(@Valid @RequestBody BankEmployee bankEmployee) {
        return bankEmployeeService.createBankEmployee(bankEmployee);
    }

    @GetMapping("/getBankEmployeeById/{employeeId}")
    public BankEmployee getBankEmployeeById(@PathVariable Long employeeId) {
        try {
            return bankEmployeeService.getBankEmployeeById(employeeId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/getAllBankEmployees")
    public List<BankEmployee> getAllBankEmployees() {
        return bankEmployeeService.getAllBankEmployees();
    }

    @PutMapping("/updateBankEmployee/{employeeId}")
    public BankEmployee updateBankEmployee(@PathVariable Long employeeId, @Valid @RequestBody BankEmployee bankEmployee) {
        try {
            return bankEmployeeService.updateBankEmployee(employeeId, bankEmployee);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/deleteBankEmployee/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBankEmployee(@PathVariable Long employeeId) {
        try {
            bankEmployeeService.deleteBankEmployee(employeeId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/getBankEmployeeByUserId/{userId}")
    public BankEmployee getBankEmployeeByUserId(@PathVariable Long userId) {
        try {
            return bankEmployeeService.getBankEmployeeByUserId(userId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}