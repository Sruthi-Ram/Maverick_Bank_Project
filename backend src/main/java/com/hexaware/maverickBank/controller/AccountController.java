package com.hexaware.maverickbank.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hexaware.maverickbank.dto.AccountCreateRequestDTO;
import com.hexaware.maverickbank.dto.AccountDTO;
import com.hexaware.maverickbank.dto.AccountUpdateRequestDTO;
import com.hexaware.maverickbank.dto.entity.Account;
import com.hexaware.maverickbank.dto.entity.BankBranch;
import com.hexaware.maverickbank.dto.entity.Customer;
import com.hexaware.maverickbank.dto.entity.Transaction;
import com.hexaware.maverickbank.service.interfaces.AccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/createaccount")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<AccountDTO> createAccount(@Valid @RequestBody AccountCreateRequestDTO accountCreateRequestDTO) {
        AccountDTO createdAccount = accountService.createAccount(accountCreateRequestDTO);
        return new ResponseEntity<>(createdAccount, HttpStatus.CREATED);
    }

    @GetMapping("/getAccountById/{accountId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'BANK_EMPLOYEE')")
    public ResponseEntity<AccountDTO> getAccountById(@PathVariable Long accountId) {
        try {
            AccountDTO accountDTO = accountService.getAccountById(accountId);
            return new ResponseEntity<>(accountDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getallaccounts")
    @PreAuthorize("hasRole('BANK_EMPLOYEE')")
    public ResponseEntity<List<AccountDTO>> getAllAccounts() {
        List<AccountDTO> accountDTOList = accountService.getAllAccounts();
        return new ResponseEntity<>(accountDTOList, HttpStatus.OK);
    }

    @PutMapping("/updateaccount/{accountId}")
    @PreAuthorize("hasRole('BANK_EMPLOYEE')")
    public ResponseEntity<AccountDTO> updateAccount(@PathVariable Long accountId, @Valid @RequestBody AccountUpdateRequestDTO accountUpdateRequestDTO) {
        try {
            AccountDTO updatedAccount = accountService.updateAccount(accountId, accountUpdateRequestDTO);
            return new ResponseEntity<>(updatedAccount, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteaccount/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('BANK_EMPLOYEE')")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long accountId) {
        try {
            accountService.deleteAccount(accountId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/customer/{customerId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<List<AccountDTO>> getAccountsByCustomerId(@PathVariable Long customerId) {
        try {
            List<AccountDTO> accountDTOList = accountService.getAccountsByCustomerId(customerId);
            return new ResponseEntity<>(accountDTOList, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/number/{accountNumber}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'BANK_EMPLOYEE')")
    public ResponseEntity<AccountDTO> getAccountByAccountNumber(@PathVariable String accountNumber) {
        try {
            AccountDTO accountDTO = accountService.getAccountByAccountNumber(accountNumber);
            return new ResponseEntity<>(accountDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/gettransactionsforaccount/{accountId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'BANK_EMPLOYEE')")
    public ResponseEntity<List<Transaction>> getTransactionsForAccount(@PathVariable Long accountId) {
        try {
            List<Transaction> transactions = accountService.getTransactionsForAccount(accountId);
            return ResponseEntity.ok(transactions);
        } catch (NoSuchElementException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/gettransactionsforaccountbydaterange/{accountId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'BANK_EMPLOYEE')")
    public ResponseEntity<List<Transaction>> getTransactionsForAccountByDateRange(@PathVariable Long accountId,
                                                                                      @RequestParam LocalDateTime startDate,
                                                                                      @RequestParam LocalDateTime endDate) {
        try {
            List<Transaction> transactions = accountService.getTransactionsForAccountByDateRange(accountId, startDate, endDate);
            return new ResponseEntity<>(transactions, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/deposit/{accountId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'BANK_EMPLOYEE')")
    public ResponseEntity<Void> deposit(@PathVariable Long accountId, @RequestParam BigDecimal amount) {
        try {
            AccountDTO accountDTO = accountService.getAccountById(accountId);
            Account account = convertDtoToEntity(accountDTO);
            accountService.deposit(account, amount);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/withdraw/{accountId}")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'BANK_EMPLOYEE')")
    public ResponseEntity<Void> withdraw(@PathVariable Long accountId, @RequestParam BigDecimal amount) {
        try {
            AccountDTO accountDTO = accountService.getAccountById(accountId);
            Account account = convertDtoToEntity(accountDTO);
            accountService.withdraw(account, amount);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/transfer")
    @PreAuthorize("hasAnyRole('CUSTOMER', 'BANK_EMPLOYEE')")
    public ResponseEntity<Void> transfer(@RequestParam String fromAccountNumber, @RequestParam String toAccountNumber, @RequestParam BigDecimal amount) {
        try {
            AccountDTO fromAccountDTO = accountService.getAccountByAccountNumber(fromAccountNumber);
            Account fromAccount = convertDtoToEntity(fromAccountDTO);
            AccountDTO toAccountDTO = accountService.getAccountByAccountNumber(toAccountNumber);
            Account toAccount = convertDtoToEntity(toAccountDTO);
            accountService.transfer(fromAccount, toAccount, amount);
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    private Account convertDtoToEntity(AccountDTO accountDTO) {
        Account account = new Account();
        account.setAccountId(accountDTO.getAccountId());
        if (accountDTO.getCustomerId() != null) {
            Customer customer = new Customer();
            customer.setCustomerId(accountDTO.getCustomerId());
            account.setCustomer(customer);
        }
        if (accountDTO.getBranchId() != null) {
            BankBranch branch = new BankBranch();
            branch.setBranchId(accountDTO.getBranchId());
            account.setBranch(branch);
        }
        account.setAccountNumber(accountDTO.getAccountNumber());
        account.setAccountType(accountDTO.getAccountType());
        account.setBalance(accountDTO.getBalance());
        account.setDateOpened(accountDTO.getDateOpened());
        account.setIfscCode(accountDTO.getIfscCode());
        return account;
    }
}