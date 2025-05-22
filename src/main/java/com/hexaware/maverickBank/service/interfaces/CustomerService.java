package com.hexaware.maverickBank.service.interfaces;

import com.hexaware.maverickBank.entity.Customer;

import java.util.List;

public interface CustomerService {
    Customer createCustomer(Customer customer);
    Customer getCustomerById(int customerId);
    List<Customer> getAllCustomers();
    Customer updateCustomer(int customerId, Customer updatedCustomer);
    void deleteCustomer(int customerId);
    Customer getCustomerByUserId(int userId);
}