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

import com.hexaware.maverickBank.dto.BeneficiaryCreateRequestDTO;
import com.hexaware.maverickBank.dto.BeneficiaryDTO;
import com.hexaware.maverickBank.dto.BeneficiaryUpdateRequestDTO;
import com.hexaware.maverickBank.service.interfaces.BeneficiaryService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/beneficiaries")
public class BeneficiaryController {

    @Autowired
    private BeneficiaryService beneficiaryService;

    @PostMapping("/createBeneficiary")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BeneficiaryDTO> createBeneficiary(@Valid @RequestBody BeneficiaryCreateRequestDTO beneficiaryCreateRequestDTO) {
        BeneficiaryDTO createdBeneficiary = beneficiaryService.createBeneficiary(beneficiaryCreateRequestDTO);
        return new ResponseEntity<>(createdBeneficiary, HttpStatus.CREATED);
    }

    @GetMapping("/getBeneficiaryById/{beneficiaryId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BeneficiaryDTO> getBeneficiaryById(@PathVariable Long beneficiaryId) {
        try {
            BeneficiaryDTO beneficiaryDTO = beneficiaryService.getBeneficiaryById(beneficiaryId);
            return new ResponseEntity<>(beneficiaryDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAllBeneficiaries")
    @PreAuthorize("hasRole('BANK_EMPLOYEE')")
    public ResponseEntity<List<BeneficiaryDTO>> getAllBeneficiaries() {
        List<BeneficiaryDTO> beneficiaryDTOList = beneficiaryService.getAllBeneficiaries();
        return new ResponseEntity<>(beneficiaryDTOList, HttpStatus.OK);
    }

    @PutMapping("/updateBeneficiary/{beneficiaryId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<BeneficiaryDTO> updateBeneficiary(@PathVariable Long beneficiaryId, @Valid @RequestBody BeneficiaryUpdateRequestDTO beneficiaryUpdateRequestDTO) {
        try {
            BeneficiaryDTO updatedBeneficiary = beneficiaryService.updateBeneficiary(beneficiaryId, beneficiaryUpdateRequestDTO);
            return new ResponseEntity<>(updatedBeneficiary, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteBeneficiary/{beneficiaryId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<Void> deleteBeneficiary(@PathVariable Long beneficiaryId) {
        try {
            beneficiaryService.deleteBeneficiary(beneficiaryId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getBeneficiariesByCustomerId/{customerId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'BANK_EMPLOYEE')")
    public ResponseEntity<List<BeneficiaryDTO>> getBeneficiariesByCustomerId(@PathVariable Long customerId) {
        try {
            List<BeneficiaryDTO> beneficiaryDTOList = beneficiaryService.getBeneficiariesByCustomerId(customerId);
            return new ResponseEntity<>(beneficiaryDTOList, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}