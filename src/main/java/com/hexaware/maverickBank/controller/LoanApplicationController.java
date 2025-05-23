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

import com.hexaware.maverickBank.entity.LoanApplication;
import com.hexaware.maverickBank.service.interfaces.LoanApplicationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/loan-applications")
public class LoanApplicationController {

    @Autowired
    private LoanApplicationService loanApplicationService;

    @PostMapping("/createLoanApplication")
    @ResponseStatus(HttpStatus.CREATED)
    public LoanApplication createLoanApplication(@Valid @RequestBody LoanApplication loanApplication) {
        return loanApplicationService.createLoanApplication(loanApplication);
    }

    @GetMapping("/getLoanApplicationById{applicationId}")
    public LoanApplication getLoanApplicationById(@PathVariable Long applicationId) {
        try {
            return loanApplicationService.getLoanApplicationById(applicationId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/getAllLoanApplications")
    public List<LoanApplication> getAllLoanApplications() {
        return loanApplicationService.getAllLoanApplications();
    }

    @PutMapping("/updateLoanApplication/{applicationId}")
    public LoanApplication updateLoanApplication(@PathVariable Long applicationId, @Valid @RequestBody LoanApplication loanApplication) {
        try {
            return loanApplicationService.updateLoanApplication(applicationId, loanApplication);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/deleteLoanApplication/{applicationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLoanApplication(@PathVariable Long applicationId) {
        try {
            loanApplicationService.deleteLoanApplication(applicationId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/getLoanApplicationsByCustomerId/{customerId}")
    public List<LoanApplication> getLoanApplicationsByCustomerId(@PathVariable Long customerId) {
        try {
            return loanApplicationService.getLoanApplicationsByCustomerId(customerId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}