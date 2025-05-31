package com.hexaware.maverickBank.controller;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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
public class BankEmployeeController {

    @Autowired
    private BankEmployeeService bankEmployeeService;

    @PostMapping("/createBankEmployee")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMINISTRATOR') or hasRole('BANK_EMPLOYEE')")
    public ResponseEntity<BankEmployeeDTO> createBankEmployee(@Valid @RequestBody BankEmployeeCreateRequestDTO bankEmployeeCreateRequestDTO) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Create Bank Employee - Authentication: " + authentication);
        System.out.println("Create Bank Employee - Authentication Details: " + (authentication != null ? authentication.getDetails() : null));
        BankEmployeeDTO createdBankEmployee = bankEmployeeService.createBankEmployee(bankEmployeeCreateRequestDTO);
        return new ResponseEntity<>(createdBankEmployee, HttpStatus.CREATED);
    }

    @GetMapping("/getBankEmployeeById/{employeeId}")
    @PreAuthorize("hasRole('BANK_EMPLOYEE') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<BankEmployeeDTO> getBankEmployeeById(@PathVariable Long employeeId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();
            String username = "";
            if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
                username = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
            }
            BankEmployeeDTO bankEmployeeDTO = bankEmployeeService.getBankEmployeeById(employeeId);
            return new ResponseEntity<>(bankEmployeeDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/getAllBankEmployees")
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<List<BankEmployeeDTO>> getAllBankEmployees() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = null;
        if (authentication != null && authentication.getDetails() instanceof Map) {
            userId = (Long) ((Map<?, ?>) authentication.getDetails()).get("userId");
        }

        System.out.println("User ID accessing getAllBankEmployees: " + userId);
        List<BankEmployeeDTO> bankEmployeeDTOList = bankEmployeeService.getAllBankEmployees();
        return new ResponseEntity<>(bankEmployeeDTOList, HttpStatus.OK);
    }

    @PutMapping("/updateBankEmployee/{employeeId}")
    @PreAuthorize("hasRole('BANK_EMPLOYEE') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<BankEmployeeDTO> updateBankEmployee(@PathVariable Long employeeId, @Valid @RequestBody BankEmployeeUpdateRequestDTO bankEmployeeUpdateRequestDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();
            String username = "";
            if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
                username = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
            }
            BankEmployeeDTO updatedBankEmployee = bankEmployeeService.updateBankEmployee(employeeId, bankEmployeeUpdateRequestDTO);
            return new ResponseEntity<>(updatedBankEmployee, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/deleteBankEmployee/{employeeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('ADMINISTRATOR')")
    public ResponseEntity<Void> deleteBankEmployee(@PathVariable Long employeeId) {
        try {
            bankEmployeeService.deleteBankEmployee(employeeId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getBankEmployeeByUserId/{userId}")
    @PreAuthorize("hasRole('BANK_EMPLOYEE') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<BankEmployeeDTO> getBankEmployeeByUserId(@PathVariable Long userId) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();
            String username = "";
            if (principal instanceof org.springframework.security.core.userdetails.UserDetails) {
                username = ((org.springframework.security.core.userdetails.UserDetails) principal).getUsername();
            }
            BankEmployeeDTO bankEmployeeDTO = bankEmployeeService.getBankEmployeeByUserId(userId);
            return new ResponseEntity<>(bankEmployeeDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.FORBIDDEN);
        }
    }
}