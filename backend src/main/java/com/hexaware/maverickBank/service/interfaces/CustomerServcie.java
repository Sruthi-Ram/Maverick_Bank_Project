package com.hexaware.maverickbank.service.interfaces;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.hexaware.maverickbank.dto.CustomerCreateRequestDTO;
import com.hexaware.maverickbank.dto.CustomerDTO;
import com.hexaware.maverickbank.dto.CustomerUpdateRequestDTO;

public interface CustomerServcie {
    CustomerDTO createCustomer(CustomerCreateRequestDTO customerCreateRequestDTO);
    CustomerDTO getCustomerById(Long customerId);
    List<CustomerDTO> getAllCustomers();
    //CustomerDTO updateCustomer(Long customerId, CustomerUpdateRequestDTO customerUpdateRequestDTO);
    void deleteCustomer(Long customerId);
    CustomerDTO getCustomerByUserId(Long userId);
	CustomerDTO updateCustomer(Long customerId, CustomerUpdateRequestDTO customerUpdateRequestDTO,
			UserDetails userDetails);
    
}