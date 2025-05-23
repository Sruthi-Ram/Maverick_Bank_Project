package com.hexaware.maverickBank.service.interfaces;

import com.hexaware.maverickBank.dto.CustomerCreateRequestDTO;
import com.hexaware.maverickBank.dto.CustomerDTO;
import com.hexaware.maverickBank.dto.CustomerUpdateRequestDTO;
import java.util.List;

public interface CustomerServcie {
    CustomerDTO createCustomer(CustomerCreateRequestDTO customerCreateRequestDTO);
    CustomerDTO getCustomerById(Long customerId);
    List<CustomerDTO> getAllCustomers();
    CustomerDTO updateCustomer(Long customerId, CustomerUpdateRequestDTO customerUpdateRequestDTO);
    void deleteCustomer(Long customerId);
    CustomerDTO getCustomerByUserId(Long userId);
}