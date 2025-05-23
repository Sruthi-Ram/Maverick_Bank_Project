package com.hexaware.maverickBank.service.interfaces;

import com.hexaware.maverickBank.entity.Account;
import com.hexaware.maverickBank.entity.Transaction;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface AccountService {
    Account createAccount(Account account);
    Account getAccountById(Long accountId);
    List<Account> getAllAccounts();
    Account updateAccount(Long accountId, Account account);
    void deleteAccount(Long accountId);
    List<Account> getAccountsByCustomerId(Long customerId);
    Account getAccountByAccountNumber(String accountNumber);
    List<Transaction> getTransactionsForAccount(Long accountId);
    List<Transaction> getTransactionsForAccountByDateRange(Long accountId, LocalDateTime startDate, LocalDateTime endDate);
    void deposit(Account account, BigDecimal amount);
    void withdraw(Account account, BigDecimal amount);
    void transfer(Account fromAccount, Account toAccount, BigDecimal amount);
}