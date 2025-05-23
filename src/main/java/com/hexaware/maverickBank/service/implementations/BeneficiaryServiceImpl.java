package com.hexaware.maverickBank.service.implementations;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexaware.maverickBank.entity.Beneficiary;
import com.hexaware.maverickBank.repository.IBeneficiaryRepository;
import com.hexaware.maverickBank.service.interfaces.BeneficiaryService;

import jakarta.validation.ValidationException;

@Service
public class BeneficiaryServiceImpl implements BeneficiaryService {

    @Autowired
    private IBeneficiaryRepository beneficiaryRepository;

    @Autowired
    private CustomerServiceImpl customerService;

    private boolean isValidIfsc(String ifsc) {
        Pattern pattern = Pattern.compile("^[A-Za-z]{4}\\d{7}$");
        Matcher matcher = pattern.matcher(ifsc);
        return matcher.matches();
    }

    @Override
    public Beneficiary createBeneficiary(Beneficiary beneficiary) {
        if (beneficiary.getCustomer() == null || beneficiary.getCustomer().getCustomerId() == null) {
            throw new ValidationException("Customer ID is required");
        }
        customerService.getCustomerById(beneficiary.getCustomer().getCustomerId()); // Ensure customer exists
        if (beneficiary.getBeneficiaryName() == null || beneficiary.getBeneficiaryName().isEmpty()) {
            throw new ValidationException("Beneficiary name cannot be empty");
        }
        if (beneficiary.getAccountNumber() == null || beneficiary.getAccountNumber().isEmpty()) {
            throw new ValidationException("Beneficiary account number cannot be empty");
        }
        if (beneficiary.getBankName() == null || beneficiary.getBankName().isEmpty()) {
            throw new ValidationException("Beneficiary bank name cannot be empty");
        }
        if (beneficiary.getBranchName() == null || beneficiary.getBranchName().isEmpty()) {
            throw new ValidationException("Beneficiary branch name cannot be empty");
        }
        if (beneficiary.getIfscCode() == null || beneficiary.getIfscCode().isEmpty() || !isValidIfsc(beneficiary.getIfscCode())) {
            throw new ValidationException("Invalid IFSC code");
        }
        // Add validation for account number format if needed
        return beneficiaryRepository.save(beneficiary);
    }

    @Override
    public Beneficiary getBeneficiaryById(Long beneficiaryId) {
        return beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new NoSuchElementException("Beneficiary not found with ID: " + beneficiaryId));
    }

    @Override
    public List<Beneficiary> getAllBeneficiaries() {
        return beneficiaryRepository.findAll();
    }

    @Override
    public Beneficiary updateBeneficiary(Long beneficiaryId, Beneficiary beneficiary) {
        Beneficiary existingBeneficiary = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new NoSuchElementException("Beneficiary not found with ID: " + beneficiaryId));
        beneficiary.setBeneficiaryId(beneficiaryId);
        // Add validation if needed
        return beneficiaryRepository.save(beneficiary);
    }

    @Override
    public void deleteBeneficiary(Long beneficiaryId) {
        if (!beneficiaryRepository.existsById(beneficiaryId)) {
            throw new NoSuchElementException("Beneficiary not found with ID: " + beneficiaryId);
        }
        beneficiaryRepository.deleteById(beneficiaryId);
    }

    @Override
    public List<Beneficiary> getBeneficiariesByCustomerId(Long customerId) {
        customerService.getCustomerById(customerId); // Ensure customer exists
        List<Beneficiary> beneficiaries = beneficiaryRepository.findByCustomer_CustomerId(customerId);
        if (beneficiaries.isEmpty()) {
            throw new NoSuchElementException("No beneficiaries found for Customer ID: " + customerId);
        }
        return beneficiaries;
    }
}