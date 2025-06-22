package com.hexaware.maverickbank.service.interfaces;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.hexaware.maverickbank.dto.AccountCreateRequestDTO;
import com.hexaware.maverickbank.dto.AccountDTO;
import com.hexaware.maverickbank.dto.AccountUpdateRequestDTO;
import com.hexaware.maverickbank.dto.entity.Account;
import com.hexaware.maverickbank.dto.entity.Transaction;

public interface AccountService {
    AccountDTO createAccount(AccountCreateRequestDTO accountCreateRequestDTO);
    AccountDTO getAccountById(Long accountId);
    List<AccountDTO> getAllAccounts();
    AccountDTO updateAccount(Long accountId, AccountUpdateRequestDTO accountUpdateRequestDTO);
    void deleteAccount(Long accountId);
    List<AccountDTO> getAccountsByCustomerId(Long customerId);
    AccountDTO getAccountByAccountNumber(String accountNumber);
    List<Transaction> getTransactionsForAccount(Long accountId);
    List<Transaction> getTransactionsForAccountByDateRange(Long accountId, LocalDateTime startDate, LocalDateTime endDate);
    void deposit(Account account, BigDecimal amount);
    void withdraw(Account account, BigDecimal amount);
    void transfer(Account fromAccount, Account toAccount, BigDecimal amount);
}