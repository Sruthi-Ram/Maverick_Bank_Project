package com.hexaware.maverickBank.service.implementations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexaware.maverickBank.entity.LoanApplication;
import com.hexaware.maverickBank.repository.ILoanApplicationRepository;
import com.hexaware.maverickBank.service.interfaces.LoanApplicationService;

import jakarta.validation.ValidationException;

@Service
public class LoanApplicationServiceImpl implements LoanApplicationService {

    @Autowired
    private ILoanApplicationRepository loanApplicationRepository;

    @Autowired
    private CustomerServiceImpl customerService;

    @Autowired
    private LoanServiceImpl loanService;

    private void validateLoanApplication(LoanApplication loanApplication) {
        if (loanApplication.getRequestedAmount() == null || loanApplication.getRequestedAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Requested loan amount must be positive");
        }
        if (loanApplication.getPurpose() == null || loanApplication.getPurpose().isEmpty()) {
            throw new ValidationException("Purpose of loan cannot be empty");
        }
        // Add more validation rules based on loan type, customer eligibility, etc.
    }

    @Override
    public LoanApplication createLoanApplication(LoanApplication loanApplication) {
        if (loanApplication.getCustomer() == null || loanApplication.getCustomer().getCustomerId() == null) {
            throw new ValidationException("Customer ID is required");
        }
        if (loanApplication.getLoan() == null || loanApplication.getLoan().getLoanId() == null) {
            throw new ValidationException("Loan ID is required");
        }
        customerService.getCustomerById(loanApplication.getCustomer().getCustomerId()); // Ensure customer exists
        loanService.getLoanById(loanApplication.getLoan().getLoanId()); // Ensure loan exists
        validateLoanApplication(loanApplication);
        loanApplication.setApplicationDate(LocalDateTime.now());
        loanApplication.setStatus("Pending"); // Default status
        return loanApplicationRepository.save(loanApplication);
    }

    @Override
    public LoanApplication getLoanApplicationById(Long applicationId) {
        return loanApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new NoSuchElementException("Loan application not found with ID: " + applicationId));
    }

    @Override
    public List<LoanApplication> getAllLoanApplications() {
        return loanApplicationRepository.findAll();
    }

    @Override
    public LoanApplication updateLoanApplication(Long applicationId, LoanApplication loanApplication) {
        LoanApplication existingApplication = loanApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new NoSuchElementException("Loan application not found with ID: " + applicationId));
        loanApplication.setApplicationId(applicationId);
        validateLoanApplication(loanApplication);
        return loanApplicationRepository.save(loanApplication);
    }

    @Override
    public void deleteLoanApplication(Long applicationId) {
        if (!loanApplicationRepository.existsById(applicationId)) {
            throw new NoSuchElementException("Loan application not found with ID: " + applicationId);
        }
        loanApplicationRepository.deleteById(applicationId);
    }

    @Override
    public List<LoanApplication> getLoanApplicationsByCustomerId(Long customerId) {
        customerService.getCustomerById(customerId); // Ensure customer exists
        List<LoanApplication> applications = loanApplicationRepository.findByCustomer_CustomerId(customerId);
        if (applications.isEmpty()) {
            throw new NoSuchElementException("No loan applications found for Customer ID: " + customerId);
        }
        return applications;
    }
}