package com.hexaware.maverickBank.service.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.hexaware.maverickBank.dto.AccountClosureRequestCreateRequestDTO;
import com.hexaware.maverickBank.dto.AccountClosureRequestDTO;
import com.hexaware.maverickBank.dto.AccountDTO;
import com.hexaware.maverickBank.dto.CustomerDTO;
import com.hexaware.maverickBank.entity.Account;
import com.hexaware.maverickBank.entity.AccountClosureRequest;
import com.hexaware.maverickBank.entity.Customer;
import com.hexaware.maverickBank.repository.IAccountClosureRequestRepository;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AccountClosureRequestServiceImplTest {

    @InjectMocks
    private AccountClosureRequestServiceImpl accountClosureRequestService;

    @Mock
    private IAccountClosureRequestRepository accountClosureRequestRepository;

    @Mock
    private CustomerServiceImpl customerService; // Add this

    @Mock
    private AccountServiceImpl accountService;   // Add this

    @Test
    void testCreateAccountClosureRequest_Success() {
        // Arrange
        AccountClosureRequestCreateRequestDTO requestToCreateDTO = new AccountClosureRequestCreateRequestDTO();
        requestToCreateDTO.setCustomerId(1L);
        requestToCreateDTO.setAccountId(1L);
        requestToCreateDTO.setReason("User request");

        AccountClosureRequest savedRequest = new AccountClosureRequest();
        savedRequest.setClosureRequestId(1L); // Simulate ID generation
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        savedRequest.setCustomer(customer);
        Account account = new Account();
        account.setAccountId(1L);
        savedRequest.setAccount(account);
        savedRequest.setReason("User request");
        savedRequest.setStatus("Pending");
        savedRequest.setRequestDate(LocalDateTime.now());

        when(accountClosureRequestRepository.save(any(AccountClosureRequest.class))).thenReturn(savedRequest);
        CustomerDTO customerDTO = new CustomerDTO(); // Create a CustomerDTO object
        customerDTO.setCustomerId(1L);           // Set the customer ID in the DTO
        when(customerService.getCustomerById(1L)).thenReturn(customerDTO); // Mock customerService call to return DTO
        AccountDTO accountDTO = new AccountDTO(); // Create an AccountDTO object
        accountDTO.setAccountId(1L);           // Set the account ID in the DTO
        accountDTO.setBalance(BigDecimal.ZERO); // Set the balance to ZERO
        when(accountService.getAccountById(1L)).thenReturn(accountDTO);   // Mock accountService call to return DTO

        // Act
        AccountClosureRequestDTO resultDTO = accountClosureRequestService.createAccountClosureRequest(requestToCreateDTO);

        // Assert
        assertNotNull(resultDTO);
        assertEquals(1L, resultDTO.getClosureRequestId());
        assertEquals(1L, resultDTO.getCustomerId());
        assertEquals(1L, resultDTO.getAccountId());
        assertEquals("User request", resultDTO.getReason());
        assertEquals("Pending", resultDTO.getStatus());
        verify(accountClosureRequestRepository, times(1)).save(any(AccountClosureRequest.class));
        verify(customerService, times(1)).getCustomerById(1L);
        verify(accountService, times(2)).getAccountById(1L); // Expecting 2 invocations
    }
    @Test
    void testGetAccountClosureRequestById_Success() {
        // Arrange
        Long requestId = 1L;
        AccountClosureRequest existingRequest = new AccountClosureRequest();
        existingRequest.setClosureRequestId(requestId);
        Customer customer = new Customer();
        customer.setCustomerId(1L);
        existingRequest.setCustomer(customer);
        Account account = new Account();
        account.setAccountId(1L);
        existingRequest.setAccount(account);
        existingRequest.setReason("Moving to a different city");
        existingRequest.setStatus("Pending");
        existingRequest.setRequestDate(LocalDateTime.now());

        when(accountClosureRequestRepository.findById(requestId)).thenReturn(Optional.of(existingRequest));

        // Act
        AccountClosureRequestDTO resultDTO = accountClosureRequestService.getAccountClosureRequestById(requestId);

        // Assert
        assertNotNull(resultDTO);
        assertEquals(requestId, resultDTO.getClosureRequestId());
        assertEquals(1L, resultDTO.getCustomerId());
        assertEquals(1L, resultDTO.getAccountId());
        assertEquals("Moving to a different city", resultDTO.getReason());
        assertEquals("Pending", resultDTO.getStatus());
        verify(accountClosureRequestRepository, times(1)).findById(requestId);
    }

    @Test
    void testGetAccountClosureRequestById_NotFound() {
        // Arrange
        Long requestId = 3L; // ID that doesn't exist in the sample data

        when(accountClosureRequestRepository.findById(requestId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NoSuchElementException.class, () -> accountClosureRequestService.getAccountClosureRequestById(requestId));
        verify(accountClosureRequestRepository, times(1)).findById(requestId);
    }
}