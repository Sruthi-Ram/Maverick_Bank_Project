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

import com.hexaware.maverickBank.entity.BankBranch;
import com.hexaware.maverickBank.service.interfaces.BankBranchService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/branches")
public class BankBranchController {

    @Autowired
    private BankBranchService bankBranchService;

    @PostMapping("/createBankBranch")
    @ResponseStatus(HttpStatus.CREATED)
    public BankBranch createBankBranch(@Valid @RequestBody BankBranch branch) {
        return bankBranchService.createBankBranch(branch);
    }

    @GetMapping("/getBankBranchById/{branchId}")
    public BankBranch getBankBranchById(@PathVariable Long branchId) {
        try {
            return bankBranchService.getBankBranchById(branchId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/getAllBankBranches")
    public List<BankBranch> getAllBankBranches() {
        return bankBranchService.getAllBankBranches();
    }

    @PutMapping("/updateBankBranch/{branchId}")
    public BankBranch updateBankBranch(@PathVariable Long branchId, @Valid @RequestBody BankBranch branch) {
        try {
            return bankBranchService.updateBankBranch(branchId, branch);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/deleteBankBranch/{branchId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBankBranch(@PathVariable Long branchId) {
        try {
            bankBranchService.deleteBankBranch(branchId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/getBankBranchByName/{name}")
    public BankBranch getBankBranchByName(@PathVariable String name) {
        try {
            return bankBranchService.getBankBranchByName(name);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/getBankBranchByIfscPrefix/{ifscPrefix}")
    public BankBranch getBankBranchByIfscPrefix(@PathVariable String ifscPrefix) {
        try {
            return bankBranchService.getBankBranchByIfscPrefix(ifscPrefix);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}