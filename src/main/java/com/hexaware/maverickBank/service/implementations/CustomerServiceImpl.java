package com.hexaware.maverickBank.service.implementations;

import com.hexaware.maverickBank.entity.Customer;
import com.hexaware.maverickBank.repository.ICustomerRepository;
import com.hexaware.maverickBank.service.interfaces.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerServiceImpl implements CustomerService {

    @Autowired
    private ICustomerRepository customerRepository;

    @Override
    @Transactional
    public Customer createCustomer(Customer customer) {
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());
        return customerRepository.save(customer);
    }

    @Override
    public Customer getCustomerById(int customerId) {
        return customerRepository.findById(customerId).orElse(null);
    }

    @Override
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    @Override
    @Transactional
    public Customer updateCustomer(int customerId, Customer updatedCustomer) {
        return customerRepository.findById(customerId)
                .map(existingCustomer -> {
                    if (updatedCustomer.getName() != null) {
                        existingCustomer.setName(updatedCustomer.getName());
                    }
                    if (updatedCustomer.getGender() != null) {
                        existingCustomer.setGender(updatedCustomer.getGender());
                    }
                    if (updatedCustomer.getContactNumber() != null) {
                        existingCustomer.setContactNumber(updatedCustomer.getContactNumber());
                    }
                    if (updatedCustomer.getAddress() != null) {
                        existingCustomer.setAddress(updatedCustomer.getAddress());
                    }
                    if (updatedCustomer.getDateOfBirth() != null) {
                        existingCustomer.setDateOfBirth(updatedCustomer.getDateOfBirth());
                    }
                    if (updatedCustomer.getAadharNumber() != null) {
                        existingCustomer.setAadharNumber(updatedCustomer.getAadharNumber());
                    }
                    if (updatedCustomer.getPanNumber() != null) {
                        existingCustomer.setPanNumber(updatedCustomer.getPanNumber());
                    }
                    existingCustomer.setUpdatedAt(LocalDateTime.now());
                    return customerRepository.save(existingCustomer);
                }).orElse(null);
    }

    @Override
    @Transactional
    public void deleteCustomer(int customerId) {
        customerRepository.deleteById(customerId);
    }

    @Override
    public Customer getCustomerByUserId(int userId) {
        return customerRepository.findByUser_UserId(userId);
    }
}