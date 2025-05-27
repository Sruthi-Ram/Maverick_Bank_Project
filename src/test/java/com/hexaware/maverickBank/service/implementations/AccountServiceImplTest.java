package com.hexaware.maverickBank.service.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import com.hexaware.maverickBank.dto.AccountCreateRequestDTO;
import com.hexaware.maverickBank.dto.AccountDTO;
import com.hexaware.maverickBank.dto.AccountUpdateRequestDTO;
import com.hexaware.maverickBank.entity.Account;
import com.hexaware.maverickBank.entity.BankBranch;
import com.hexaware.maverickBank.entity.Customer;
import com.hexaware.maverickBank.entity.Transaction;
import com.hexaware.maverickBank.repository.IAccountRepository;
import com.hexaware.maverickBank.repository.IBankBranchRepository;
import com.hexaware.maverickBank.repository.ICustomerRepository;
import com.hexaware.maverickBank.repository.ITransactionRepository;

import jakarta.validation.ValidationException;

class AccountServiceImplTest {
	@Mock
    private IAccountRepository accountRepository;

    @Mock
    private ITransactionRepository transactionRepository;

    @Mock
    private ICustomerRepository customerRepository;

    @Mock
    private IBankBranchRepository bankBranchRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateAccount_Success() {
        // Arrange
        AccountCreateRequestDTO createRequestDTO = new AccountCreateRequestDTO();
        createRequestDTO.setCustomerId(1L);
        createRequestDTO.setBranchId(101L);
        createRequestDTO.setAccountType("Savings");
        createRequestDTO.setBalance(BigDecimal.TEN);
        createRequestDTO.setIfscCode("MAV000001");

        Customer customer = new Customer();
        customer.setCustomerId(1L);

        BankBranch branch = new BankBranch();
        branch.setBranchId(101L);

        Account savedAccount = new Account();
        savedAccount.setAccountId(123L);
        savedAccount.setAccountNumber("generatedAccountNumber");
        savedAccount.setAccountType("Savings");
        savedAccount.setBalance(BigDecimal.TEN);
        savedAccount.setIfscCode("MAV000001");
        savedAccount.setCustomer(customer);
        savedAccount.setBranch(branch);

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(bankBranchRepository.findById(101L)).thenReturn(Optional.of(branch));
        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);

        // Act
        AccountDTO accountDTO = accountService.createAccount(createRequestDTO);

        // Assert
        assertNotNull(accountDTO);
        assertEquals(123L, accountDTO.getAccountId());
        assertEquals("generatedAccountNumber", accountDTO.getAccountNumber());
        assertEquals("Savings", accountDTO.getAccountType());
        assertEquals(BigDecimal.TEN, accountDTO.getBalance());
        assertEquals("MAV000001", accountDTO.getIfscCode());
        assertEquals(1L, accountDTO.getCustomerId());
        assertEquals(101L, accountDTO.getBranchId());
    }

    @Test
    void testCreateAccount_InvalidCustomerId() {
        // Arrange
        AccountCreateRequestDTO createRequestDTO = new AccountCreateRequestDTO();
        createRequestDTO.setCustomerId(99L); // Non-existent customer ID
        createRequestDTO.setBranchId(101L);
        createRequestDTO.setAccountType("Savings");
        createRequestDTO.setBalance(BigDecimal.TEN);
        createRequestDTO.setIfscCode("MAV000001");

        when(customerRepository.findById(99L)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(ValidationException.class, () -> accountService.createAccount(createRequestDTO));
    }

    // You can add more test cases for other scenarios, like invalid branch ID, etc.


	@Test
    void testGetAccountById_Success() {
        // Arrange
        Long accountId = 1L;
        Account mockAccount = new Account();
        mockAccount.setAccountId(accountId);
        // Set other properties of mockAccount

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));

        // Act
        AccountDTO accountDTO = accountService.getAccountById(accountId);

        // Assert
        assertNotNull(accountDTO);
        assertEquals(accountId, accountDTO.getAccountId());
        // Assert other properties
    }

    @Test
    void testGetAccountById_NotFound() {
        // Arrange
        Long accountId = 1L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(NoSuchElementException.class, () -> accountService.getAccountById(accountId));
    }

    @Test
    void testGetAllAccounts_Success() {
        // Arrange
        Account account1 = new Account();
        account1.setAccountId(1L);
        Account account2 = new Account();
        account2.setAccountId(2L);
        List<Account> mockAccounts = List.of(account1, account2);

        when(accountRepository.findAll()).thenReturn(mockAccounts);

        // Act
        List<AccountDTO> accountDTOs = accountService.getAllAccounts();

        // Assert
        assertNotNull(accountDTOs);
        assertEquals(2, accountDTOs.size());
        assertEquals(1L, accountDTOs.get(0).getAccountId());
        assertEquals(2L, accountDTOs.get(1).getAccountId());
    }

    @Test
    void testGetAllAccounts_EmptyList() {
        // Arrange
        when(accountRepository.findAll()).thenReturn(List.of());

        // Act
        List<AccountDTO> accountDTOs = accountService.getAllAccounts();

        // Assert
        assertNotNull(accountDTOs);
        assertTrue(accountDTOs.isEmpty());
    }
    @Test
    void testUpdateAccount_Success() {
        // Arrange
        Long accountId = 1L;
        AccountUpdateRequestDTO updateRequestDTO = new AccountUpdateRequestDTO();
        updateRequestDTO.setAccountType("Current");
        BigDecimal newBalance = new BigDecimal("100.00");
        updateRequestDTO.setBalance(newBalance);

        Account existingAccount = new Account();
        existingAccount.setAccountId(accountId);
        existingAccount.setAccountType("Savings");
        existingAccount.setBalance(BigDecimal.TEN);

        Account updatedAccount = new Account();
        updatedAccount.setAccountId(accountId);
        updatedAccount.setAccountType("Current");
        updatedAccount.setBalance(newBalance);

        when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));
        when(accountRepository.save(any(Account.class))).thenReturn(updatedAccount);

        // Act
        AccountDTO accountDTO = accountService.updateAccount(accountId, updateRequestDTO);

        // Assert
        assertNotNull(accountDTO);
        assertEquals(accountId, accountDTO.getAccountId());
        assertEquals("Current", accountDTO.getAccountType());
        assertEquals(newBalance, accountDTO.getBalance());
    }

    @Test
    void testUpdateAccount_NotFound() {
        // Arrange
        Long accountId = 1L;
        AccountUpdateRequestDTO updateRequestDTO = new AccountUpdateRequestDTO();
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(NoSuchElementException.class, () -> accountService.updateAccount(accountId, updateRequestDTO));
    }

    @Test
    void testDeleteAccount_Success() {
        // Arrange
        Long accountId = 1L;
        when(accountRepository.existsById(accountId)).thenReturn(true);
        // Act
        accountService.deleteAccount(accountId);
        // Assert
        Mockito.verify(accountRepository, Mockito.times(1)).deleteById(accountId);
    }

    @Test
    void testDeleteAccount_NotFound() {
        // Arrange
        Long accountId = 1L;
        when(accountRepository.existsById(accountId)).thenReturn(false);
        // Act and Assert
        assertThrows(NoSuchElementException.class, () -> accountService.deleteAccount(accountId));
        Mockito.verify(accountRepository, Mockito.never()).deleteById(accountId);
    }

    @Test
    void testGetAccountsByCustomerId_Success() {
        // Arrange
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setCustomerId(customerId);
        Account account1 = new Account();
        account1.setAccountId(101L);
        account1.setCustomer(customer);
        Account account2 = new Account();
        account2.setAccountId(102L);
        account2.setCustomer(customer);
        List<Account> mockAccounts = List.of(account1, account2);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(accountRepository.findByCustomer_CustomerId(customerId)).thenReturn(mockAccounts);

        // Act
        List<AccountDTO> accountDTOs = accountService.getAccountsByCustomerId(customerId);

        // Assert
        assertNotNull(accountDTOs);
        assertEquals(2, accountDTOs.size());
        assertEquals(customerId, accountDTOs.get(0).getCustomerId());
        assertEquals(customerId, accountDTOs.get(1).getCustomerId());
    }

    @Test
    void testGetAccountsByCustomerId_CustomerNotFound() {
        // Arrange
        Long customerId = 1L;
        when(customerRepository.findById(customerId)).thenReturn(Optional.empty());

        // Act and Assert
        assertThrows(NoSuchElementException.class, () -> accountService.getAccountsByCustomerId(customerId));
    }

    @Test
    void testGetAccountsByCustomerId_NoAccountsFound() {
        // Arrange
        Long customerId = 1L;
        Customer customer = new Customer();
        customer.setCustomerId(customerId);

        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(accountRepository.findByCustomer_CustomerId(customerId)).thenReturn(List.of());

        // Act and Assert
        assertThrows(NoSuchElementException.class, () -> accountService.getAccountsByCustomerId(customerId));
    }

    @Test
    void testGetAccountByAccountNumber_Success() {
        // Arrange
        String accountNumber = "1234567890";
        Account mockAccount = new Account();
        mockAccount.setAccountNumber(accountNumber);
        // Set other properties

        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(mockAccount);

        // Act
        AccountDTO accountDTO = accountService.getAccountByAccountNumber(accountNumber);

        // Assert
        assertNotNull(accountDTO);
        assertEquals(accountNumber, accountDTO.getAccountNumber());
        // Assert other properties
    }

    @Test
    void testGetAccountByAccountNumber_NotFound() {
        // Arrange
        String accountNumber = "1234567890";
        when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(null);

        // Act and Assert
        assertThrows(NoSuchElementException.class, () -> accountService.getAccountByAccountNumber(accountNumber));
    }

    @Test
    void testGetTransactionsForAccount_Success() {
        // Arrange
        Long accountId = 1L;
        Account mockAccount = new Account();
        mockAccount.setAccountId(accountId);
        Transaction transaction1 = Mockito.mock(Transaction.class);
        Transaction transaction2 = Mockito.mock(Transaction.class);
        List<Transaction> mockTransactions = List.of(transaction1, transaction2);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));
        when(transactionRepository.findByAccount_AccountIdOrderByTransactionDateDesc(accountId)).thenReturn(mockTransactions);
        // Act
        List<Transaction> transactions = accountService.getTransactionsForAccount(accountId);
        // Assert
        assertNotNull(transactions);
        assertEquals(2, transactions.size());
    }

    @Test
    void testGetTransactionsForAccount_AccountNotFound() {
        // Arrange
        Long accountId = 1L;
        when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
        // Act and Assert
        assertThrows(NoSuchElementException.class, () -> accountService.getTransactionsForAccount(accountId));
    }

    @Test
    void testGetTransactionsForAccount_NoTransactions() {
        // Arrange
        Long accountId = 1L;
        Account mockAccount = new Account();
        mockAccount.setAccountId(accountId);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));
        when(transactionRepository.findByAccount_AccountIdOrderByTransactionDateDesc(accountId)).thenReturn(List.of());
        // Act
        List<Transaction> transactions = accountService.getTransactionsForAccount(accountId);
        // Assert
        assertNotNull(transactions);
        assertTrue(transactions.isEmpty());
    }
    @Test
    void testGetTransactionsForAccountByDateRange_Success() {
        // Arrange
        Long accountId = 1L;
        Account mockAccount = new Account();
        mockAccount.setAccountId(accountId);
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();
        Transaction transaction1 = Mockito.mock(Transaction.class);
        Transaction transaction2 = Mockito.mock(Transaction.class);
        List<Transaction> mockTransactions = List.of(transaction1, transaction2);
        when(accountRepository.findById(accountId)).thenReturn(Optional.of(mockAccount));
        when(transactionRepository.findByAccount_AccountIdAndTransactionDateBetweenOrderByTransactionDateDesc(
                Mockito.eq(accountId), Mockito.any(), Mockito.any()
        )).thenReturn(mockTransactions);
        // Act
        List<Transaction> transactions = accountService.getTransactionsForAccountByDateRange(accountId, startDate, endDate);
        // Assert
        assertNotNull(transactions);
        assertEquals(2, transactions.size());
    }
    @Test
    void testDeposit_Success() {
        // Arrange
        Account account = new Account();
        account.setAccountId(1L);
        account.setBalance(BigDecimal.TEN);
        BigDecimal depositAmount = new BigDecimal("50.00");
        // Act
        accountService.deposit(account, depositAmount);
        // Assert
        assertEquals(new BigDecimal("60.00"), account.getBalance());
        Mockito.verify(accountRepository).save(account);
        Mockito.verify(transactionRepository).save(any(Transaction.class));
    }

    @Test
    void testDeposit_InvalidAmount() {
        // Arrange
        Account account = new Account();
        BigDecimal depositAmount = new BigDecimal("-10.00");
        // Act and Assert
        assertThrows(ValidationException.class, () -> accountService.deposit(account, depositAmount));
        Mockito.verify(accountRepository, Mockito.never()).save(any(Account.class));
        Mockito.verify(transactionRepository, Mockito.never()).save(any(Transaction.class));
    }


}
