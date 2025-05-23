package com.hexaware.maverickBank.service.implementations;

import com.hexaware.maverickBank.entity.Account;
import com.hexaware.maverickBank.entity.BankBranch;
import com.hexaware.maverickBank.entity.Customer;
import com.hexaware.maverickBank.entity.Transaction;
import com.hexaware.maverickBank.exception.InsufficientBalanceException;
import com.hexaware.maverickBank.exception.InvalidTransferAmountException;
import com.hexaware.maverickBank.repository.IAccountRepository;
import com.hexaware.maverickBank.repository.ITransactionRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.validation.ValidationException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class AccountServiceImplTest {

    @Mock
    private IAccountRepository accountRepository;

    @Mock
    private ITransactionRepository transactionRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    @Test
    void createAccount() {
        Account account = new Account();
        Mockito.when(accountRepository.save(account)).thenReturn(account);
        Assertions.assertNotNull(accountService.createAccount(account));
    }

    @Test
    void getAccountById_Found() {
        Long accountId = 1L;
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(new Account()));
        Assertions.assertNotNull(accountService.getAccountById(accountId));
    }

    @Test
    void getAccountById_NotFound() {
        Long accountId = 1L;
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> accountService.getAccountById(accountId));
    }

    @Test
    void getAllAccounts() {
        List<Account> accounts = new ArrayList<>();
        Mockito.when(accountRepository.findAll()).thenReturn(accounts);
        Assertions.assertEquals(accounts, accountService.getAllAccounts());
    }

    @Test
    void updateAccount_Found() {
        Long accountId = 1L;
        Account existingAccount = new Account();
        Account updatedAccount = new Account();
        updatedAccount.setAccountId(accountId);
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(existingAccount));
        Mockito.when(accountRepository.save(updatedAccount)).thenReturn(updatedAccount);
        Assertions.assertEquals(updatedAccount, accountService.updateAccount(accountId, updatedAccount));
    }

    @Test
    void updateAccount_NotFound() {
        Long accountId = 1L;
        Account updatedAccount = new Account();
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> accountService.updateAccount(accountId, updatedAccount));
    }

    @Test
    void deleteAccount_Found() {
        Long accountId = 1L;
        Mockito.when(accountRepository.existsById(accountId)).thenReturn(true);
        Assertions.assertDoesNotThrow(() -> accountService.deleteAccount(accountId));
        Mockito.verify(accountRepository).deleteById(accountId);
    }

    @Test
    void deleteAccount_NotFound() {
        Long accountId = 1L;
        Mockito.when(accountRepository.existsById(accountId)).thenReturn(false);
        Assertions.assertThrows(NoSuchElementException.class, () -> accountService.deleteAccount(accountId));
        Mockito.verify(accountRepository, Mockito.never()).deleteById(accountId);
    }

    @Test
    void getAccountsByCustomerId_Found() {
        Long customerId = 1L;
        Mockito.when(accountRepository.findByCustomer_CustomerId(Mockito.anyLong())).thenReturn(List.of(new Account()));
        Assertions.assertNotNull(accountService.getAccountsByCustomerId(customerId));
    }

    @Test
    void getAccountsByCustomerId_NotFound() {
        Long customerId = 1L;
        Mockito.when(accountRepository.findByCustomer_CustomerId(Mockito.anyLong())).thenReturn(List.of());
        Assertions.assertThrows(NoSuchElementException.class, () -> accountService.getAccountsByCustomerId(customerId));
    }

    @Test
    void getAccountByAccountNumber_Found() {
        String accountNumber = "123456";
        Mockito.when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(new Account());
        Assertions.assertNotNull(accountService.getAccountByAccountNumber(accountNumber));
    }

    @Test
    void getAccountByAccountNumber_NotFound() {
        String accountNumber = "123456";
        Mockito.when(accountRepository.findByAccountNumber(accountNumber)).thenReturn(null);
        Assertions.assertThrows(NoSuchElementException.class, () -> accountService.getAccountByAccountNumber(accountNumber));
    }

    @Test
    void getTransactionsForAccount_Found() {
        Long accountId = 1L;
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(new Account()));
        Mockito.when(transactionRepository.findByAccount_AccountIdOrderByTransactionDateDesc(accountId)).thenReturn(List.of(new Transaction()));
        Assertions.assertNotNull(accountService.getTransactionsForAccount(accountId));
    }

    @Test
    void getTransactionsForAccount_NotFoundAccount() {
        Long accountId = 1L;
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> accountService.getTransactionsForAccount(accountId));
    }

    @Test
    void getTransactionsForAccountByDateRange_Found() {
        Long accountId = 1L;
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.of(new Account()));
        Mockito.when(transactionRepository.findByAccount_AccountIdAndTransactionDateBetweenOrderByTransactionDateDesc(Mockito.eq(accountId), Mockito.any(), Mockito.any())).thenReturn(List.of(new Transaction()));
        Assertions.assertNotNull(accountService.getTransactionsForAccountByDateRange(accountId, startDate, endDate));
    }

    @Test
    void getTransactionsForAccountByDateRange_NotFoundAccount() {
        Long accountId = 1L;
        LocalDateTime startDate = LocalDateTime.now().minusDays(7);
        LocalDateTime endDate = LocalDateTime.now();
        Mockito.when(accountRepository.findById(accountId)).thenReturn(Optional.empty());
        Assertions.assertThrows(NoSuchElementException.class, () -> accountService.getTransactionsForAccountByDateRange(accountId, startDate, endDate));
    }

    @Test
    void deposit_Success() {
        Account account = new Account();
        account.setBalance(BigDecimal.ZERO);
        BigDecimal amount = BigDecimal.TEN;
        Mockito.when(accountRepository.save(account)).thenReturn(account);
        Assertions.assertDoesNotThrow(() -> accountService.deposit(account, amount));
        Assertions.assertEquals(BigDecimal.TEN, account.getBalance());
        Mockito.verify(transactionRepository).save(Mockito.any(Transaction.class));
    }

    @Test
    void deposit_InvalidAmount() {
        Account account = new Account();
        BigDecimal amount = BigDecimal.valueOf(-10);
        Assertions.assertThrows(ValidationException.class, () -> accountService.deposit(account, amount));
        Mockito.verify(accountRepository, Mockito.never()).save(Mockito.any(Account.class));
        Mockito.verify(transactionRepository, Mockito.never()).save(Mockito.any(Transaction.class));
    }

    @Test
    void withdraw_Success() {
        Account account = new Account();
        account.setBalance(BigDecimal.TEN);
        BigDecimal amount = BigDecimal.ONE;
        Mockito.when(accountRepository.save(account)).thenReturn(account);
        Assertions.assertDoesNotThrow(() -> accountService.withdraw(account, amount));
        Assertions.assertEquals(BigDecimal.valueOf(9), account.getBalance());
        Mockito.verify(transactionRepository).save(Mockito.any(Transaction.class));
    }

    @Test
    void withdraw_InvalidAmount() {
        Account account = new Account();
        BigDecimal amount = BigDecimal.valueOf(-10);
        Assertions.assertThrows(ValidationException.class, () -> accountService.withdraw(account, amount));
        Mockito.verify(accountRepository, Mockito.never()).save(Mockito.any(Account.class));
        Mockito.verify(transactionRepository, Mockito.never()).save(Mockito.any(Transaction.class));
    }

    @Test
    void withdraw_InsufficientBalance() {
        Account account = new Account();
        account.setBalance(BigDecimal.ONE);
        BigDecimal amount = BigDecimal.TEN;
        Assertions.assertThrows(InsufficientBalanceException.class, () -> accountService.withdraw(account, amount));
        Mockito.verify(accountRepository, Mockito.never()).save(Mockito.any(Account.class));
        Mockito.verify(transactionRepository, Mockito.never()).save(Mockito.any(Transaction.class));
    }

    @Test
    void transfer_Success() {
        Account fromAccount = new Account();
        fromAccount.setAccountId(1L);
        fromAccount.setAccountNumber("123");
        fromAccount.setBalance(BigDecimal.TEN);
        Account toAccount = new Account();
        toAccount.setAccountId(2L);
        toAccount.setAccountNumber("456");
        toAccount.setBalance(BigDecimal.ZERO);
        BigDecimal amount = BigDecimal.ONE;

        Mockito.when(accountRepository.save(fromAccount)).thenReturn(fromAccount);
        Mockito.when(accountRepository.save(toAccount)).thenReturn(toAccount);

        accountService.transfer(fromAccount, toAccount, amount);

        Assertions.assertEquals(BigDecimal.valueOf(9), fromAccount.getBalance());
        Assertions.assertEquals(BigDecimal.ONE, toAccount.getBalance());
        Mockito.verify(transactionRepository, Mockito.times(2)).save(Mockito.any(Transaction.class));
    }

    @Test
    void transfer_InvalidAmount() {
        Account fromAccount = new Account();
        Account toAccount = new Account();
        BigDecimal amount = BigDecimal.valueOf(-10);
        Assertions.assertThrows(InvalidTransferAmountException.class, () -> accountService.transfer(fromAccount, toAccount, amount));
        Mockito.verify(accountRepository, Mockito.never()).save(Mockito.any(Account.class));
        Mockito.verify(transactionRepository, Mockito.never()).save(Mockito.any(Transaction.class));
    }

    @Test
    void transfer_InsufficientBalance() {
        Account fromAccount = new Account();
        fromAccount.setBalance(BigDecimal.ONE);
        Account toAccount = new Account();
        BigDecimal amount = BigDecimal.TEN;
        Assertions.assertThrows(InsufficientBalanceException.class, () -> accountService.transfer(fromAccount, toAccount, amount));
        Mockito.verify(accountRepository, Mockito.never()).save(Mockito.any(Account.class));
        Mockito.verify(transactionRepository, Mockito.never()).save(Mockito.any(Transaction.class));
    }

    @Test
    void transfer_SameAccount() {
        Account account = new Account();
        account.setAccountId(1L);
        BigDecimal amount = BigDecimal.ONE;
        Assertions.assertThrows(ValidationException.class, () -> accountService.transfer(account, account, amount));
        Mockito.verify(accountRepository, Mockito.never()).save(Mockito.any(Account.class));
        Mockito.verify(transactionRepository, Mockito.never()).save(Mockito.any(Transaction.class));
    }
}