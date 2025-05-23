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

import com.hexaware.maverickBank.entity.Beneficiary;
import com.hexaware.maverickBank.service.interfaces.BeneficiaryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/beneficiaries")
public class BeneficiaryController {

    @Autowired
    private BeneficiaryService beneficiaryService;

    @PostMapping("/createBeneficiary")
    @ResponseStatus(HttpStatus.CREATED)
    public Beneficiary createBeneficiary(@Valid @RequestBody Beneficiary beneficiary) {
        return beneficiaryService.createBeneficiary(beneficiary);
    }

    @GetMapping("/getBeneficiaryById/{beneficiaryId}")
    public Beneficiary getBeneficiaryById(@PathVariable Long beneficiaryId) {
        try {
            return beneficiaryService.getBeneficiaryById(beneficiaryId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/getAllBeneficiaries")
    public List<Beneficiary> getAllBeneficiaries() {
        return beneficiaryService.getAllBeneficiaries();
    }

    @PutMapping("/updateBeneficiary/{beneficiaryId}")
    public Beneficiary updateBeneficiary(@PathVariable Long beneficiaryId, @Valid @RequestBody Beneficiary beneficiary) {
        try {
            return beneficiaryService.updateBeneficiary(beneficiaryId, beneficiary);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/deleteBeneficiary/{beneficiaryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBeneficiary(@PathVariable Long beneficiaryId) {
        try {
            beneficiaryService.deleteBeneficiary(beneficiaryId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/getBeneficiariesByCustomerId/{customerId}")
    public List<Beneficiary> getBeneficiariesByCustomerId(@PathVariable Long customerId) {
        try {
            return beneficiaryService.getBeneficiariesByCustomerId(customerId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}