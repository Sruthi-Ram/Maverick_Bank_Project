package com.hexaware.maverickBank.service.implementations;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexaware.maverickBank.dto.BeneficiaryCreateRequestDTO;
import com.hexaware.maverickBank.dto.BeneficiaryDTO;
import com.hexaware.maverickBank.dto.BeneficiaryUpdateRequestDTO;
import com.hexaware.maverickBank.entity.Beneficiary;
import com.hexaware.maverickBank.entity.Customer;
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
    public BeneficiaryDTO createBeneficiary(BeneficiaryCreateRequestDTO beneficiaryCreateRequestDTO) {
        if (beneficiaryCreateRequestDTO.getCustomerId() == null) {
            throw new ValidationException("Customer ID is required");
        }
        customerService.getCustomerById(beneficiaryCreateRequestDTO.getCustomerId()); // Ensure customer exists
        if (beneficiaryCreateRequestDTO.getBeneficiaryName() == null || beneficiaryCreateRequestDTO.getBeneficiaryName().isEmpty()) {
            throw new ValidationException("Beneficiary name cannot be empty");
        }
        if (beneficiaryCreateRequestDTO.getAccountNumber() == null || beneficiaryCreateRequestDTO.getAccountNumber().isEmpty()) {
            throw new ValidationException("Beneficiary account number cannot be empty");
        }
        if (beneficiaryCreateRequestDTO.getBankName() == null || beneficiaryCreateRequestDTO.getBankName().isEmpty()) {
            throw new ValidationException("Beneficiary bank name cannot be empty");
        }
        if (beneficiaryCreateRequestDTO.getBranchName() == null || beneficiaryCreateRequestDTO.getBranchName().isEmpty()) {
            throw new ValidationException("Beneficiary branch name cannot be empty");
        }
        if (beneficiaryCreateRequestDTO.getIfscCode() == null || beneficiaryCreateRequestDTO.getIfscCode().isEmpty() || !isValidIfsc(beneficiaryCreateRequestDTO.getIfscCode())) {
            throw new ValidationException("Invalid IFSC code");
        }
        // Add validation for account number format if needed
        Beneficiary beneficiary = convertCreateRequestDTOtoEntity(beneficiaryCreateRequestDTO);
        Beneficiary savedBeneficiary = beneficiaryRepository.save(beneficiary);
        return convertEntityToDTO(savedBeneficiary);
    }

    @Override
    public BeneficiaryDTO getBeneficiaryById(Long beneficiaryId) {
        Beneficiary beneficiary = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new NoSuchElementException("Beneficiary not found with ID: " + beneficiaryId));
        return convertEntityToDTO(beneficiary);
    }

    @Override
    public List<BeneficiaryDTO> getAllBeneficiaries() {
        return beneficiaryRepository.findAll().stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public BeneficiaryDTO updateBeneficiary(Long beneficiaryId, BeneficiaryUpdateRequestDTO beneficiaryUpdateRequestDTO) {
        Beneficiary existingBeneficiary = beneficiaryRepository.findById(beneficiaryId)
                .orElseThrow(() -> new NoSuchElementException("Beneficiary not found with ID: " + beneficiaryId));
        if (beneficiaryUpdateRequestDTO.getBeneficiaryName() != null && !beneficiaryUpdateRequestDTO.getBeneficiaryName().isEmpty()) {
            existingBeneficiary.setBeneficiaryName(beneficiaryUpdateRequestDTO.getBeneficiaryName());
        }
        if (beneficiaryUpdateRequestDTO.getAccountNumber() != null && !beneficiaryUpdateRequestDTO.getAccountNumber().isEmpty()) {
            existingBeneficiary.setAccountNumber(beneficiaryUpdateRequestDTO.getAccountNumber());
        }
        if (beneficiaryUpdateRequestDTO.getBankName() != null && !beneficiaryUpdateRequestDTO.getBankName().isEmpty()) {
            existingBeneficiary.setBankName(beneficiaryUpdateRequestDTO.getBankName());
        }
        if (beneficiaryUpdateRequestDTO.getBranchName() != null && !beneficiaryUpdateRequestDTO.getBranchName().isEmpty()) {
            existingBeneficiary.setBranchName(beneficiaryUpdateRequestDTO.getBranchName());
        }
        if (beneficiaryUpdateRequestDTO.getIfscCode() != null && !beneficiaryUpdateRequestDTO.getIfscCode().isEmpty()) {
            if (!isValidIfsc(beneficiaryUpdateRequestDTO.getIfscCode())) {
                throw new ValidationException("Invalid IFSC code");
            }
            existingBeneficiary.setIfscCode(beneficiaryUpdateRequestDTO.getIfscCode());
        }
        Beneficiary updatedBeneficiary = beneficiaryRepository.save(existingBeneficiary);
        return convertEntityToDTO(updatedBeneficiary);
    }

    @Override
    public void deleteBeneficiary(Long beneficiaryId) {
        if (!beneficiaryRepository.existsById(beneficiaryId)) {
            throw new NoSuchElementException("Beneficiary not found with ID: " + beneficiaryId);
        }
        beneficiaryRepository.deleteById(beneficiaryId);
    }

    @Override
    public List<BeneficiaryDTO> getBeneficiariesByCustomerId(Long customerId) {
        customerService.getCustomerById(customerId); // Ensure customer exists
        List<Beneficiary> beneficiaries = beneficiaryRepository.findByCustomer_CustomerId(customerId);
        if (beneficiaries.isEmpty()) {
            throw new NoSuchElementException("No beneficiaries found for Customer ID: " + customerId);
        }
        return beneficiaries.stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    private BeneficiaryDTO convertEntityToDTO(Beneficiary beneficiary) {
        BeneficiaryDTO dto = new BeneficiaryDTO();
        dto.setBeneficiaryId(beneficiary.getBeneficiaryId());
        if (beneficiary.getCustomer() != null) {
            dto.setCustomerId(beneficiary.getCustomer().getCustomerId());
        }
        dto.setBeneficiaryName(beneficiary.getBeneficiaryName());
        dto.setAccountNumber(beneficiary.getAccountNumber());
        dto.setBankName(beneficiary.getBankName());
        dto.setBranchName(beneficiary.getBranchName());
        dto.setIfscCode(beneficiary.getIfscCode());
        return dto;
    }

    private Beneficiary convertCreateRequestDTOtoEntity(BeneficiaryCreateRequestDTO createRequestDTO) {
        Beneficiary beneficiary = new Beneficiary();
        Customer customer = new Customer();
        customer.setCustomerId(createRequestDTO.getCustomerId());
        beneficiary.setCustomer(customer);
        beneficiary.setBeneficiaryName(createRequestDTO.getBeneficiaryName());
        beneficiary.setAccountNumber(createRequestDTO.getAccountNumber());
        beneficiary.setBankName(createRequestDTO.getBankName());
        beneficiary.setBranchName(createRequestDTO.getBranchName());
        beneficiary.setIfscCode(createRequestDTO.getIfscCode());
        return beneficiary;
    }
}