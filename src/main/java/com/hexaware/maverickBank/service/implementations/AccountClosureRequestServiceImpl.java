package com.hexaware.maverickBank.service.implementations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexaware.maverickBank.dto.AccountClosureRequestCreateRequestDTO;
import com.hexaware.maverickBank.dto.AccountClosureRequestDTO;
import com.hexaware.maverickBank.entity.Account;
import com.hexaware.maverickBank.entity.AccountClosureRequest;
import com.hexaware.maverickBank.entity.Customer;
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

    private void validateAccountClosureRequest(AccountClosureRequestDTO requestDTO) {
        if (requestDTO.getCustomerId() == null) {
            throw new ValidationException("Customer ID is required");
        }
        if (requestDTO.getAccountId() == null) {
            throw new ValidationException("Account ID is required");
        }
        if (requestDTO.getReason() == null || requestDTO.getReason().isEmpty()) {
            throw new ValidationException("Reason for closure is required");
        }
        if (accountService.getAccountById(requestDTO.getAccountId()).getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new ValidationException("Account must have zero balance to be closed");
        }
        // You might add more validation or business logic here (e.g., approval workflow)
    }

    @Override
    public AccountClosureRequestDTO createAccountClosureRequest(AccountClosureRequestCreateRequestDTO requestDTO) {
        if (requestDTO.getCustomerId() == null) {
            throw new ValidationException("Customer ID is required");
        }
        if (requestDTO.getAccountId() == null) {
            throw new ValidationException("Account ID is required");
        }
        customerService.getCustomerById(requestDTO.getCustomerId()); // Ensure customer exists
        accountService.getAccountById(requestDTO.getAccountId()); // Ensure account exists
        AccountClosureRequest request = convertCreateRequestDTOtoEntity(requestDTO);
        validateAccountClosureRequest(convertEntityToDTO(request));
        request.setRequestDate(LocalDateTime.now());
        request.setStatus("Pending"); // Default status
        AccountClosureRequest savedRequest = accountClosureRequestRepository.save(request);
        return convertEntityToDTO(savedRequest);
    }

    @Override
    public AccountClosureRequestDTO getAccountClosureRequestById(Long closureRequestId) {
        AccountClosureRequest request = accountClosureRequestRepository.findById(closureRequestId)
                .orElseThrow(() -> new NoSuchElementException("Account closure request not found with ID: " + closureRequestId));
        return convertEntityToDTO(request);
    }

    @Override
    public List<AccountClosureRequestDTO> getAllAccountClosureRequests() {
        return accountClosureRequestRepository.findAll().stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AccountClosureRequestDTO updateAccountClosureRequest(Long closureRequestId, AccountClosureRequestDTO requestDTO) {
        AccountClosureRequest existingRequest = accountClosureRequestRepository.findById(closureRequestId)
                .orElseThrow(() -> new NoSuchElementException("Account closure request not found with ID: " + closureRequestId));
        AccountClosureRequest request = convertDTOtoEntity(requestDTO);
        request.setClosureRequestId(closureRequestId);
        validateAccountClosureRequest(requestDTO);
        AccountClosureRequest updatedRequest = accountClosureRequestRepository.save(request);
        return convertEntityToDTO(updatedRequest);
    }

    @Override
    public void deleteAccountClosureRequest(Long closureRequestId) {
        if (!accountClosureRequestRepository.existsById(closureRequestId)) {
            throw new NoSuchElementException("Account closure request not found with ID: " + closureRequestId);
        }
        accountClosureRequestRepository.deleteById(closureRequestId);
    }

    @Override
    public List<AccountClosureRequestDTO> getAccountClosureRequestsByCustomerId(Long customerId) {
        customerService.getCustomerById(customerId); // Ensure customer exists
        List<AccountClosureRequest> requests = accountClosureRequestRepository.findByCustomer_CustomerId(customerId);
        if (requests.isEmpty()) {
            throw new NoSuchElementException("No account closure requests found for Customer ID: " + customerId);
        }
        return requests.stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AccountClosureRequestDTO getAccountClosureRequestByAccountId(Long accountId) {
        accountService.getAccountById(accountId); // Ensure account exists
        AccountClosureRequest request = accountClosureRequestRepository.findByAccount_AccountId(accountId);
        if (request == null) {
            throw new NoSuchElementException("Account closure request not found for Account ID: " + accountId);
        }
        return convertEntityToDTO(request);
    }

    private AccountClosureRequestDTO convertEntityToDTO(AccountClosureRequest request) {
        AccountClosureRequestDTO dto = new AccountClosureRequestDTO();
        dto.setClosureRequestId(request.getClosureRequestId());
        if (request.getCustomer() != null) {
            dto.setCustomerId(request.getCustomer().getCustomerId());
        }
        if (request.getAccount() != null) {
            dto.setAccountId(request.getAccount().getAccountId());
        }
        dto.setRequestDate(request.getRequestDate());
        dto.setReason(request.getReason());
        dto.setStatus(request.getStatus());
        return dto;
    }

    private AccountClosureRequest convertCreateRequestDTOtoEntity(AccountClosureRequestCreateRequestDTO createRequestDTO) {
        AccountClosureRequest request = new AccountClosureRequest();
        Customer customer = new Customer();
        customer.setCustomerId(createRequestDTO.getCustomerId());
        request.setCustomer(customer);
        Account account = new Account();
        account.setAccountId(createRequestDTO.getAccountId());
        request.setAccount(account);
        request.setReason(createRequestDTO.getReason());
        return request;
    }

    private AccountClosureRequest convertDTOtoEntity(AccountClosureRequestDTO requestDTO) {
        AccountClosureRequest request = new AccountClosureRequest();
        request.setClosureRequestId(requestDTO.getClosureRequestId());
        Customer customer = new Customer();
        customer.setCustomerId(requestDTO.getCustomerId());
        request.setCustomer(customer);
        Account account = new Account();
        account.setAccountId(requestDTO.getAccountId());
        request.setAccount(account);
        request.setRequestDate(requestDTO.getRequestDate());
        request.setReason(requestDTO.getReason());
        request.setStatus(requestDTO.getStatus());
        return request;
    }
}