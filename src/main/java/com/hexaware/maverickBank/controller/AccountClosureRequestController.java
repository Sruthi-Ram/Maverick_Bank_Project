package com.hexaware.maverickBank.controller;

import com.hexaware.maverickBank.entity.AccountClosureRequest;
import com.hexaware.maverickBank.service.interfaces.AccountClosureRequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import jakarta.validation.Valid;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/v1/account-closure-requests")
public class AccountClosureRequestController {

    @Autowired
    private AccountClosureRequestService accountClosureRequestService;

    @PostMapping("/createaccountclosurerequest")
    @ResponseStatus(HttpStatus.CREATED)
    public AccountClosureRequest createAccountClosureRequest(@Valid @RequestBody AccountClosureRequest request) {
        return accountClosureRequestService.createAccountClosureRequest(request);
    }

    @GetMapping("/getaccountclosurerequestbyid/{closureRequestId}")
    public AccountClosureRequest getAccountClosureRequestById(@PathVariable Long closureRequestId) {
        try {
            return accountClosureRequestService.getAccountClosureRequestById(closureRequestId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/getallaccountclosurerequests")
    public List<AccountClosureRequest> getAllAccountClosureRequests() {
        return accountClosureRequestService.getAllAccountClosureRequests();
    }

    @PutMapping("/updateaccountclosurerequest/{closureRequestId}")
    public AccountClosureRequest updateAccountClosureRequest(@PathVariable Long closureRequestId, @Valid @RequestBody AccountClosureRequest request) {
        try {
            return accountClosureRequestService.updateAccountClosureRequest(closureRequestId, request);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/deleteaccountclosurerequest/{closureRequestId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccountClosureRequest(@PathVariable Long closureRequestId) {
        try {
            accountClosureRequestService.deleteAccountClosureRequest(closureRequestId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/getAccountClosureRequestsByCustomerId/{customerId}")
    public List<AccountClosureRequest> getAccountClosureRequestsByCustomerId(@PathVariable Long customerId) {
        try {
            return accountClosureRequestService.getAccountClosureRequestsByCustomerId(customerId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/getAccountClosureRequestByAccountId/{accountId}")
    public AccountClosureRequest getAccountClosureRequestByAccountId(@PathVariable Long accountId) {
        try {
            return accountClosureRequestService.getAccountClosureRequestByAccountId(accountId);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        }
    }
}