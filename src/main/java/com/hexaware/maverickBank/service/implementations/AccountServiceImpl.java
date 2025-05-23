package com.hexaware.maverickBank.service.implementations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hexaware.maverickBank.entity.Account;
import com.hexaware.maverickBank.entity.Transaction;
import com.hexaware.maverickBank.exception.InsufficientBalanceException;
import com.hexaware.maverickBank.exception.InvalidTransferAmountException;
import com.hexaware.maverickBank.repository.IAccountRepository;
import com.hexaware.maverickBank.repository.IBankBranchRepository;
import com.hexaware.maverickBank.repository.ICustomerRepository;
import com.hexaware.maverickBank.repository.ITransactionRepository;
import com.hexaware.maverickBank.service.interfaces.AccountService;

import jakarta.validation.ValidationException;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private IAccountRepository accountRepository;

    @Autowired
    private ITransactionRepository transactionRepository;

    @Autowired
    private ICustomerRepository customerRepository;

    @Autowired
    private IBankBranchRepository bankBranchRepository;

    private void validateAccount(Account account) {
        if (account.getCustomer() == null || account.getCustomer().getCustomerId() == null || customerRepository.findById(account.getCustomer().getCustomerId()).isEmpty()) {
            throw new ValidationException("Customer ID is required and must exist");
        }
        if (account.getBranch() == null || account.getBranch().getBranchId() == null || bankBranchRepository.findById(account.getBranch().getBranchId()).isEmpty()) {
            throw new ValidationException("Branch ID is required and must exist");
        }
        if (account.getAccountType() == null || account.getAccountType().isEmpty()) {
            throw new ValidationException("Account type cannot be empty");
        }
        if (account.getBalance() == null || account.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Initial balance must be zero or positive");
        }
    }

    private String generateAccountNumber() {
        // Simple account number generation logic (you might need something more sophisticated)
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    @Override
    public Account createAccount(Account account) {
        validateAccount(account);
        account.setDateOpened(LocalDateTime.now());
        account.setAccountNumber(generateAccountNumber());
        // You might want to set IFSC code based on the branch
        return accountRepository.save(account);
    }

    @Override
    public Account getAccountById(Long accountId) {
        return accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("Account not found with ID: " + accountId));
    }

    @Override
    public List<Account> getAllAccounts() {
        return accountRepository.findAll();
    }

    @Override
    public Account updateAccount(Long accountId, Account account) {
        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("Account not found with ID: " + accountId));
        account.setAccountId(accountId);
        account.setDateOpened(existingAccount.getDateOpened()); // Keep the original opening date
        validateAccount(account);
        return accountRepository.save(account);
    }

    @Override
    public void deleteAccount(Long accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new NoSuchElementException("Account not found with ID: " + accountId);
        }
        // Consider adding logic to prevent deleting accounts with a balance
        accountRepository.deleteById(accountId);
    }

    @Override
    public List<Account> getAccountsByCustomerId(Long customerId) {
        getCustomerByIdForAccount(customerId); // Ensure customer exists
        List<Account> accounts = accountRepository.findByCustomer_CustomerId(customerId);
        if (accounts.isEmpty()) {
            throw new NoSuchElementException("No accounts found for Customer ID: " + customerId);
        }
        return accounts;
    }

    @Override
    public Account getAccountByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new NoSuchElementException("Account not found with account number: " + accountNumber);
        }
        return account;
    }

    @Override
    public List<Transaction> getTransactionsForAccount(Long accountId) {
        getAccountById(accountId); // Throws exception if account not found
        List<Transaction> transactions = transactionRepository.findByAccount_AccountIdOrderByTransactionDateDesc(accountId);
        if (transactions.isEmpty()) {
            throw new NoSuchElementException("No transactions found for Account ID: " + accountId);
        }
        return transactions;
    }

    @Override
    public List<Transaction> getTransactionsForAccountByDateRange(Long accountId, LocalDateTime startDate, LocalDateTime endDate) {
        getAccountById(accountId); // Throws exception if account not found
        List<Transaction> transactions = transactionRepository.findByAccount_AccountIdAndTransactionDateBetweenOrderByTransactionDateDesc(accountId, startDate, endDate);
        if (transactions.isEmpty()) {
            throw new NoSuchElementException("No transactions found for Account ID: " + accountId + " within the given date range.");
        }
        return transactions;
    }

    @Override
    @Transactional
    public void deposit(Account account, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Deposit amount must be positive");
        }
        // Additional business logic if needed (e.g., limits)
        account.setBalance(account.getBalance().add(amount));
        accountRepository.save(account);
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setTransactionType("Deposit");
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public void withdraw(Account account, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Withdrawal amount must be positive");
        }
        if (account.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance in the account");
        }
        // Additional business logic if needed (e.g., daily withdrawal limits)
        account.setBalance(account.getBalance().subtract(amount));
        accountRepository.save(account);
        Transaction transaction = new Transaction();
        transaction.setAccount(account);
        transaction.setTransactionType("Withdrawal");
        transaction.setAmount(amount);
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public void transfer(Account fromAccount, Account toAccount, BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidTransferAmountException("Transfer amount must be positive");
        }
        if (fromAccount.getAccountId().equals(toAccount.getAccountId())) {
            throw new ValidationException("Cannot transfer to the same account");
        }
        if (fromAccount.getBalance().compareTo(amount) < 0) {
            throw new InsufficientBalanceException("Insufficient balance in the source account");
        }
        // Additional business logic if needed (e.g., transfer limits)
        withdraw(fromAccount, amount);
        deposit(toAccount, amount);
        Transaction transaction = new Transaction();
        transaction.setAccount(fromAccount);
        transaction.setTransactionType("Transfer");
        transaction.setAmount(amount);
        transaction.setDescription("Transfer to Account: " + toAccount.getAccountNumber());
        transaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(transaction);
        Transaction receiveTransaction = new Transaction();
        receiveTransaction.setAccount(toAccount);
        receiveTransaction.setTransactionType("Transfer");
        receiveTransaction.setAmount(amount);
        receiveTransaction.setDescription("Transfer from Account: " + fromAccount.getAccountNumber());
        receiveTransaction.setTransactionDate(LocalDateTime.now());
        transactionRepository.save(receiveTransaction);
    }

    private void getCustomerByIdForAccount(Long customerId) {
        customerRepository.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("Customer not found with ID: " + customerId));
    }
}