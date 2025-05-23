package com.hexaware.maverickBank.service.interfaces;

import com.hexaware.maverickBank.entity.Customer;
import java.util.List;

public interface CustomerServcie {
    Customer createCustomer(Customer customer);
    Customer getCustomerById(Long customerId);
    List<Customer> getAllCustomers();
    Customer updateCustomer(Long customerId, Customer customer);
    void deleteCustomer(Long customerId);
    Customer getCustomerByUserId(Long userId);
}