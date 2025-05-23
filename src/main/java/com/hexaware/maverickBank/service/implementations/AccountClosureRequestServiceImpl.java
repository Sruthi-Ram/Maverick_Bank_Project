package com.hexaware.maverickBank.service.implementations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexaware.maverickBank.entity.AccountClosureRequest;
import com.hexaware.maverickBank.repository.IAccountClosureRequestRepository;
import com.hexaware.maverickBank.service.interfaces.AccountClosureRequestService;

import jakarta.validation.ValidationException;

@Service
public class AccountClosureRequestServiceImpl implements AccountClosureRequestService {

    @Autowired
    private IAccountClosureRequestRepository accountClosureRequestRepository;

    @Autowired
    private CustomerServiceImpl customerService;

    @Autowired
    private AccountServiceImpl accountService;

    private void validateAccountClosureRequest(AccountClosureRequest request) {
        if (request.getCustomer() == null || request.getCustomer().getCustomerId() == null) {
            throw new ValidationException("Customer ID is required");
        }
        if (request.getAccount() == null || request.getAccount().getAccountId() == null) {
            throw new ValidationException("Account ID is required");
        }
        if (request.getReason() == null || request.getReason().isEmpty()) {
            throw new ValidationException("Reason for closure is required");
        }
        if (accountService.getAccountById(request.getAccount().getAccountId()).getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new ValidationException("Account must have zero balance to be closed");
        }
        // You might add more validation or business logic here (e.g., approval workflow)
    }

    @Override
    public AccountClosureRequest createAccountClosureRequest(AccountClosureRequest request) {
        if (request.getCustomer() == null || request.getCustomer().getCustomerId() == null) {
            throw new ValidationException("Customer ID is required");
        }
        if (request.getAccount() == null || request.getAccount().getAccountId() == null) {
            throw new ValidationException("Account ID is required");
        }
        customerService.getCustomerById(request.getCustomer().getCustomerId()); // Ensure customer exists
        accountService.getAccountById(request.getAccount().getAccountId()); // Ensure account exists
        validateAccountClosureRequest(request);
        request.setRequestDate(LocalDateTime.now());
        request.setStatus("Pending"); // Default status
        return accountClosureRequestRepository.save(request);
    }

    @Override
    public AccountClosureRequest getAccountClosureRequestById(Long closureRequestId) {
        return accountClosureRequestRepository.findById(closureRequestId)
                .orElseThrow(() -> new NoSuchElementException("Account closure request not found with ID: " + closureRequestId));
    }

    @Override
    public List<AccountClosureRequest> getAllAccountClosureRequests() {
        return accountClosureRequestRepository.findAll();
    }

    @Override
    public AccountClosureRequest updateAccountClosureRequest(Long closureRequestId, AccountClosureRequest request) {
        AccountClosureRequest existingRequest = accountClosureRequestRepository.findById(closureRequestId)
                .orElseThrow(() -> new NoSuchElementException("Account closure request not found with ID: " + closureRequestId));
        request.setClosureRequestId(closureRequestId);
        validateAccountClosureRequest(request);
        return accountClosureRequestRepository.save(request);
    }

    @Override
    public void deleteAccountClosureRequest(Long closureRequestId) {
        if (!accountClosureRequestRepository.existsById(closureRequestId)) {
            throw new NoSuchElementException("Account closure request not found with ID: " + closureRequestId);
        }
        accountClosureRequestRepository.deleteById(closureRequestId);
    }

    @Override
    public List<AccountClosureRequest> getAccountClosureRequestsByCustomerId(Long customerId) {
        customerService.getCustomerById(customerId); // Ensure customer exists
        List<AccountClosureRequest> requests = accountClosureRequestRepository.findByCustomer_CustomerId(customerId);
        if (requests.isEmpty()) {
            throw new NoSuchElementException("No account closure requests found for Customer ID: " + customerId);
        }
        return requests;
    }

    @Override
    public AccountClosureRequest getAccountClosureRequestByAccountId(Long accountId) {
        accountService.getAccountById(accountId); // Ensure account exists
        AccountClosureRequest request = accountClosureRequestRepository.findByAccount_AccountId(accountId);
        if (request == null) {
            throw new NoSuchElementException("Account closure request not found for Account ID: " + accountId);
        }
        return request;
    }
}