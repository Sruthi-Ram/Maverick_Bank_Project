package com.hexaware.maverickBank.controller;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hexaware.maverickBank.entity.Customer;
import com.hexaware.maverickBank.service.interfaces.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping("/createcustomer")
    @ResponseStatus(HttpStatus.CREATED)
    public Customer createCustomer(@Valid @RequestBody Customer customer) {
        return customerService.createCustomer(customer);
    }

    @GetMapping("/getcustomerbyid/{customerId}")
    public Customer getCustomerById(@PathVariable int customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        if (customer == null) {
            throw new NoSuchElementException("Customer not found with ID: " + customerId);
        }
        return customer;
    }

    @GetMapping("/getallcustomers")
    public List<Customer> getAllCustomers() {
        return customerService.getAllCustomers();
    }

    @PutMapping("/updatecustomer/{customerId}")
    public Customer updateCustomer(@PathVariable int customerId, @Valid @RequestBody Customer updatedCustomer) {
        Customer customer = customerService.updateCustomer(customerId, updatedCustomer);
        if (customer == null) {
            throw new NoSuchElementException("Customer not found with ID: " + customerId);
        }
        return customer;
    }

    @DeleteMapping("/deletecustomer/{customerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCustomer(@PathVariable int customerId) {
        Customer customer = customerService.getCustomerById(customerId);
        if (customer == null) {
            throw new NoSuchElementException("Customer not found with ID: " + customerId);
        }
        customerService.deleteCustomer(customerId);
    }

    @GetMapping("/getcustomerbyuserid/{userId}")
    public Customer getCustomerByUserId(@PathVariable int userId) {
        Customer customer = customerService.getCustomerByUserId(userId);
        if (customer == null) {
            throw new NoSuchElementException("Customer not found for User ID: " + userId);
        }
        return customer;
    }
}