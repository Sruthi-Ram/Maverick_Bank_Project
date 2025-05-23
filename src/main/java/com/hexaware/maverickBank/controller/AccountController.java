package com.hexaware.maverickBank.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import org.springframework.web.server.ResponseStatusException;

import com.hexaware.maverickBank.entity.Account;
import com.hexaware.maverickBank.entity.Transaction;
import com.hexaware.maverickBank.service.interfaces.AccountService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/accounts")
public class AccountController {

    @Autowired
    private AccountService accountService;

    @PostMapping("/createaccount")
    @ResponseStatus(HttpStatus.CREATED)
    public Account createAccount(@Valid @RequestBody Account account) {
        return accountService.createAccount(account);
    }

    @GetMapping("/getAccountById/{accountId}")
    public Account getAccountById(@PathVariable Long accountId) {
        try {
            return accountService.getAccountById(accountId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/getallaccounts")
    public List<Account> getAllAccounts() {
        return accountService.getAllAccounts();
    }

    @PutMapping("/updateaccount/{accountId}")
    public Account updateAccount(@PathVariable Long accountId, @Valid @RequestBody Account account) {
        try {
            return accountService.updateAccount(accountId, account);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/deleteaccount/{accountId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable Long accountId) {
        try {
            accountService.deleteAccount(accountId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/customer/{customerId}")
    public List<Account> getAccountsByCustomerId(@PathVariable Long customerId) {
        try {
            return accountService.getAccountsByCustomerId(customerId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/number/{accountNumber}")
    public Account getAccountByAccountNumber(@PathVariable String accountNumber) {
        try {
            return accountService.getAccountByAccountNumber(accountNumber);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/gettransactionsforaccount/{accountId}")
    public List<Transaction> getTransactionsForAccount(@PathVariable Long accountId) {
        try {
            return accountService.getTransactionsForAccount(accountId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/gettransactionsforaccountbydaterange/{accountId}")
    public List<Transaction> getTransactionsForAccountByDateRange(@PathVariable Long accountId,
                                                                   @RequestParam LocalDateTime startDate,
                                                                   @RequestParam LocalDateTime endDate) {
        try {
            return accountService.getTransactionsForAccountByDateRange(accountId, startDate, endDate);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @PostMapping("/deposit/{accountId}")
    public void deposit(@PathVariable Long accountId, @RequestParam BigDecimal amount) {
        Account account = accountService.getAccountById(accountId);
        accountService.deposit(account, amount);
    }

    @PostMapping("/withdraw/{accountId}")
    public void withdraw(@PathVariable Long accountId, @RequestParam BigDecimal amount) {
        Account account = accountService.getAccountById(accountId);
        accountService.withdraw(account, amount);
    }

    @PostMapping("/transfer")
    public void transfer(@RequestParam String fromAccountNumber, @RequestParam String toAccountNumber, @RequestParam BigDecimal amount) {
        Account fromAccount = accountService.getAccountByAccountNumber(fromAccountNumber);
        Account toAccount = accountService.getAccountByAccountNumber(toAccountNumber);
        accountService.transfer(fromAccount, toAccount, amount);
    }
}