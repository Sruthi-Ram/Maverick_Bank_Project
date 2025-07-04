package com.hexaware.maverickbank.controller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hexaware.maverickbank.dto.CustomerCreateRequestDTO;
import com.hexaware.maverickbank.dto.CustomerDTO;
import com.hexaware.maverickbank.dto.CustomerUpdateRequestDTO;
import com.hexaware.maverickbank.service.interfaces.CustomerServcie;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    @Autowired
    private CustomerServcie customerService;

    @PostMapping("/createCustomer")
    @PreAuthorize("hasRole('CUSTOMER')")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerCreateRequestDTO customerCreateRequestDTO) {
        CustomerDTO createdCustomer = customerService.createCustomer(customerCreateRequestDTO);
        return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
    }

    @GetMapping("/getCustomerById/{customerId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long customerId) {
        try {
            CustomerDTO customerDTO = customerService.getCustomerById(customerId);
            return new ResponseEntity<>(customerDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getAllCustomers")
    @PreAuthorize("hasRole('BANK_EMPLOYEE')")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customerDTOList = customerService.getAllCustomers();
        return new ResponseEntity<>(customerDTOList, HttpStatus.OK);
    }

    @PutMapping("/updateCustomer/{customerId}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('BANK_EMPLOYEE') or hasRole('ADMINISTRATOR')")
    public ResponseEntity<CustomerDTO> updateCustomer(@PathVariable Long customerId, @Valid @RequestBody CustomerUpdateRequestDTO customerUpdateRequestDTO) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            CustomerDTO updatedCustomer = customerService.updateCustomer(customerId, customerUpdateRequestDTO, userDetails);
            return new ResponseEntity<>(updatedCustomer, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/deleteCustomer/{customerId}")
    @PreAuthorize("hasRole('BANK_EMPLOYEE') or hasRole('ADMINISTRATOR')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public ResponseEntity<Void> deleteCustomer(@PathVariable Long customerId) {
        try {
            customerService.deleteCustomer(customerId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/getCustomerByUserId/{userId}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('BANK_EMPLOYEE')")
    public ResponseEntity<CustomerDTO> getCustomerByUserId(@PathVariable Long userId) {
        try {
            CustomerDTO customerDTO = customerService.getCustomerByUserId(userId);
            return new ResponseEntity<>(customerDTO, HttpStatus.OK);
        } catch (NoSuchElementException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}