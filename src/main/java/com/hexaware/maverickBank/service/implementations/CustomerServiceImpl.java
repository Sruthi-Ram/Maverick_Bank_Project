package com.hexaware.maverickBank.service.implementations;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexaware.maverickBank.entity.Customer;
import com.hexaware.maverickBank.repository.ICustomerRepository;
import com.hexaware.maverickBank.repository.IUserRepository;
import com.hexaware.maverickBank.service.interfaces.CustomerServcie;

import jakarta.validation.ValidationException;

@Service
public class CustomerServiceImpl implements CustomerServcie {

    @Autowired
    private ICustomerRepository customerRepository;

    @Autowired
    private IUserRepository userRepository;

    private void validateCustomer(Customer customer) {
        if (customer.getName() == null || customer.getName().isEmpty()) {
            throw new ValidationException("Customer name cannot be empty");
        }
        if (customer.getDateOfBirth() == null) {
            throw new ValidationException("Date of birth is required");
        }
        LocalDate now = LocalDate.now();
        Period age = Period.between(customer.getDateOfBirth(), now);
        if (age.getYears() < 18) {
            throw new ValidationException("Customer must be at least 18 years old");
        }
        if (customer.getAddress() == null || customer.getAddress().isEmpty()) {
            throw new ValidationException("Address cannot be empty");
        }
        if (customer.getContactNumber() == null || customer.getContactNumber().isEmpty()) {
            throw new ValidationException("Contact number cannot be empty");
        }
        // Add more validation rules as needed for gender, aadhar, pan, etc.
    }

    @Override
    public Customer createCustomer(Customer customer) {
        if (customer.getUser() == null || customer.getUser().getUserId() == null || userRepository.findById(customer.getUser().getUserId()).isEmpty()) {
            throw new ValidationException("User ID is required and must exist");
        }
        validateCustomer(customer);
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomerById(Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("Customer not found with ID: " + customerId));
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    public Customer updateCustomer(Long customerId, Customer customer) {
        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("Customer not found with ID: " + customerId));
        customer.setCustomerId(customerId);
        validateCustomer(customer);
        return customerRepository.save(customer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new NoSuchElementException("Customer not found with ID: " + customerId);
        }
        // Consider adding logic to prevent deleting customers with active accounts or loans
        customerRepository.deleteById(customerId);
    }

    @Override
    public Customer getCustomerByUserId(Long userId) {
        Customer customer = customerRepository.findByUser_UserId(userId);
        if (customer == null) {
            throw new NoSuchElementException("Customer not found for User ID: " + userId);
        }
        return customer;
    }
}