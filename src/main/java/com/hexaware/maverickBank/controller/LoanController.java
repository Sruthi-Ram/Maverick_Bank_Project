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

import com.hexaware.maverickBank.entity.Loan;
import com.hexaware.maverickBank.service.interfaces.LoanService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/loans")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @PostMapping("/createLoan")
    @ResponseStatus(HttpStatus.CREATED)
    public Loan createLoan(@Valid @RequestBody Loan loan) {
        return loanService.createLoan(loan);
    }

    @GetMapping("/getLoanById/{loanId}")
    public Loan getLoanById(@PathVariable Long loanId) {
        try {
            return loanService.getLoanById(loanId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/getAllLoans")
    public List<Loan> getAllLoans() {
        return loanService.getAllLoans();
    }

    @PutMapping("/updateLoan{loanId}")
    public Loan updateLoan(@PathVariable Long loanId, @Valid @RequestBody Loan loan) {
        try {
            return loanService.updateLoan(loanId, loan);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/deleteLoan{loanId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLoan(@PathVariable Long loanId) {
        try {
            loanService.deleteLoan(loanId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}