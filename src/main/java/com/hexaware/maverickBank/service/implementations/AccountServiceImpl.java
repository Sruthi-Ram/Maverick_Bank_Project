package com.hexaware.maverickBank.service.implementations;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.hexaware.maverickBank.dto.AccountCreateRequestDTO;
import com.hexaware.maverickBank.dto.AccountDTO;
import com.hexaware.maverickBank.dto.AccountUpdateRequestDTO;
import com.hexaware.maverickBank.entity.Account;
import com.hexaware.maverickBank.entity.BankBranch;
import com.hexaware.maverickBank.entity.Customer;
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

    private void validateAccount(AccountDTO accountDTO) {
        if (accountDTO.getCustomerId() == null || customerRepository.findById(accountDTO.getCustomerId()).isEmpty()) {
            throw new ValidationException("Customer ID is required and must exist");
        }
        if (accountDTO.getBranchId() == null || bankBranchRepository.findById(accountDTO.getBranchId()).isEmpty()) {
            throw new ValidationException("Branch ID is required and must exist");
        }
        if (accountDTO.getAccountType() == null || accountDTO.getAccountType().isEmpty()) {
            throw new ValidationException("Account type cannot be empty");
        }
        if (accountDTO.getBalance() == null || accountDTO.getBalance().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Initial balance must be zero or positive");
        }
    }

    private String generateAccountNumber() {
        // Simple account number generation logic (you might need something more sophisticated)
        return UUID.randomUUID().toString().replace("-", "").substring(0, 16);
    }

    @Override
    public AccountDTO createAccount(AccountCreateRequestDTO accountCreateRequestDTO) {
        validateAccount(convertCreateRequestDTOtoDTO(accountCreateRequestDTO));
        Account account = convertCreateRequestDTOtoEntity(accountCreateRequestDTO);
        account.setDateOpened(LocalDateTime.now());
        account.setAccountNumber(generateAccountNumber());
        // You might want to set IFSC code based on the branch
        Account savedAccount = accountRepository.save(account);
        return convertEntityToDTO(savedAccount);
    }

    @Override
    public AccountDTO getAccountById(Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("Account not found with ID: " + accountId));
        return convertEntityToDTO(account);
    }

    @Override
    public List<AccountDTO> getAllAccounts() {
        return accountRepository.findAll().stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDTO updateAccount(Long accountId, AccountUpdateRequestDTO accountUpdateRequestDTO) {
        Account existingAccount = accountRepository.findById(accountId)
                .orElseThrow(() -> new NoSuchElementException("Account not found with ID: " + accountId));
        if (accountUpdateRequestDTO.getAccountType() != null) {
            existingAccount.setAccountType(accountUpdateRequestDTO.getAccountType());
        }
        if (accountUpdateRequestDTO.getBalance() != null) {
            existingAccount.setBalance(accountUpdateRequestDTO.getBalance());
        }
        if (accountUpdateRequestDTO.getIfscCode() != null) {
            existingAccount.setIfscCode(accountUpdateRequestDTO.getIfscCode());
        }
        existingAccount.setDateOpened(existingAccount.getDateOpened()); 
        Account updatedAccount = accountRepository.save(existingAccount);
        return convertEntityToDTO(updatedAccount);
    }

    @Override
    public void deleteAccount(Long accountId) {
        if (!accountRepository.existsById(accountId)) {
            throw new NoSuchElementException("Account not found with ID: " + accountId);
        }
        
        accountRepository.deleteById(accountId);
    }

    @Override
    public List<AccountDTO> getAccountsByCustomerId(Long customerId) {
        getCustomerByIdForAccount(customerId); 
        List<Account> accounts = accountRepository.findByCustomer_CustomerId(customerId);
        if (accounts.isEmpty()) {
            throw new NoSuchElementException("No accounts found for Customer ID: " + customerId);
        }
        return accounts.stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AccountDTO getAccountByAccountNumber(String accountNumber) {
        Account account = accountRepository.findByAccountNumber(accountNumber);
        if (account == null) {
            throw new NoSuchElementException("Account not found with account number: " + accountNumber);
        }
        return convertEntityToDTO(account);
    }

    @Override
    public List<Transaction> getTransactionsForAccount(Long accountId) {
        getAccountById(accountId); 
        return transactionRepository.findByAccount_AccountIdOrderByTransactionDateDesc(accountId);
    }

    @Override
    public List<Transaction> getTransactionsForAccountByDateRange(Long accountId, LocalDateTime startDate, LocalDateTime endDate) {
        getAccountById(accountId); 
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

    private AccountDTO convertEntityToDTO(Account account) {
        AccountDTO dto = new AccountDTO();
        dto.setAccountId(account.getAccountId());
        if (account.getCustomer() != null) {
            dto.setCustomerId(account.getCustomer().getCustomerId());
        }
        if (account.getBranch() != null) {
            dto.setBranchId(account.getBranch().getBranchId());
        }
        dto.setAccountNumber(account.getAccountNumber());
        dto.setAccountType(account.getAccountType());
        dto.setBalance(account.getBalance());
        dto.setDateOpened(account.getDateOpened());
        dto.setIfscCode(account.getIfscCode());
        return dto;
    }

    private Account convertCreateRequestDTOtoEntity(AccountCreateRequestDTO createRequestDTO) {
        Account account = new Account();
        Customer customer = new Customer();
        customer.setCustomerId(createRequestDTO.getCustomerId());
        account.setCustomer(customer);
        BankBranch branch = new BankBranch();
        branch.setBranchId(createRequestDTO.getBranchId());
        account.setBranch(branch);
        account.setAccountType(createRequestDTO.getAccountType());
        account.setBalance(createRequestDTO.getBalance());
        account.setIfscCode(createRequestDTO.getIfscCode());
        return account;
    }

    private AccountDTO convertCreateRequestDTOtoDTO(AccountCreateRequestDTO createRequestDTO) {
        AccountDTO dto = new AccountDTO();
        dto.setCustomerId(createRequestDTO.getCustomerId());
        dto.setBranchId(createRequestDTO.getBranchId());
        dto.setAccountType(createRequestDTO.getAccountType());
        dto.setBalance(createRequestDTO.getBalance());
        dto.setIfscCode(createRequestDTO.getIfscCode());
        return dto;
    }
}