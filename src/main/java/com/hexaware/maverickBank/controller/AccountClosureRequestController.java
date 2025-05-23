package com.hexaware.maverickBank.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hexaware.maverickBank.dto.AccountClosureRequestCreateRequestDTO;
import com.hexaware.maverickBank.dto.AccountClosureRequestDTO;
import com.hexaware.maverickBank.service.interfaces.AccountClosureRequestService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/account-closure-requests")
public class AccountClosureRequestController {

    @Autowired
    private AccountClosureRequestService accountClosureRequestService;

    @PostMapping("/createaccountclosurerequest")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<AccountClosureRequestDTO> createAccountClosureRequest(@Valid @RequestBody AccountClosureRequestCreateRequestDTO requestDTO) {
        AccountClosureRequestDTO createdRequest = accountClosureRequestService.createAccountClosureRequest(requestDTO);
        return new ResponseEntity<>(createdRequest, HttpStatus.CREATED);
    }

    @GetMapping("/getaccountclosurerequestbyid/{closureRequestId}")
    public ResponseEntity<AccountClosureRequestDTO> getAccountClosureRequestById(@PathVariable Long closureRequestId) {
        try {
            AccountClosureRequestDTO requestDTO = accountClosureRequestService.getAccountClosureRequestById(closureRequestId);
            return new ResponseEntity<>(requestDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getallaccountclosurerequests")
    public ResponseEntity<List<AccountClosureRequestDTO>> getAllAccountClosureRequests() {
        List<AccountClosureRequestDTO> requestDTOList = accountClosureRequestService.getAllAccountClosureRequests();
        return new ResponseEntity<>(requestDTOList, HttpStatus.OK);
    }

    @PutMapping("/updateaccountclosurerequest/{closureRequestId}")
    public ResponseEntity<AccountClosureRequestDTO> updateAccountClosureRequest(@PathVariable Long closureRequestId, @Valid @RequestBody AccountClosureRequestDTO requestDTO) {
        try {
            AccountClosureRequestDTO updatedRequest = accountClosureRequestService.updateAccountClosureRequest(closureRequestId, requestDTO);
            return new ResponseEntity<>(updatedRequest, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteaccountclosurerequest/{closureRequestId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteAccountClosureRequest(@PathVariable Long closureRequestId) {
        try {
            accountClosureRequestService.deleteAccountClosureRequest(closureRequestId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAccountClosureRequestsByCustomerId/{customerId}")
    public ResponseEntity<List<AccountClosureRequestDTO>> getAccountClosureRequestsByCustomerId(@PathVariable Long customerId) {
        try {
            List<AccountClosureRequestDTO> requestDTOList = accountClosureRequestService.getAccountClosureRequestsByCustomerId(customerId);
            return new ResponseEntity<>(requestDTOList, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAccountClosureRequestByAccountId/{accountId}")
    public ResponseEntity<AccountClosureRequestDTO> getAccountClosureRequestByAccountId(@PathVariable Long accountId) {
        try {
            AccountClosureRequestDTO requestDTO = accountClosureRequestService.getAccountClosureRequestByAccountId(accountId);
            return new ResponseEntity<>(requestDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}