package com.hexaware.maverickBank.service.implementations;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.hexaware.maverickBank.dto.CustomerCreateRequestDTO;
import com.hexaware.maverickBank.dto.CustomerDTO;
import com.hexaware.maverickBank.dto.CustomerUpdateRequestDTO;
import com.hexaware.maverickBank.entity.Customer;
import com.hexaware.maverickBank.entity.User;
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
        Period age = Period.between(customer.getDateOfBirth(), LocalDate.now());
        if (age.getYears() < 18) {
            throw new ValidationException("Customer must be at least 18 years old");
        }
        if (customer.getAddress() == null || customer.getAddress().isEmpty()) {
            throw new ValidationException("Address cannot be empty");
        }
        if (customer.getContactNumber() == null || customer.getContactNumber().isEmpty()) {
            throw new ValidationException("Contact number cannot be empty");
        }
    }

    @Override
    public CustomerDTO createCustomer(CustomerCreateRequestDTO customerCreateRequestDTO) {
        if (customerCreateRequestDTO.getUserId() == null ||
            userRepository.findById(customerCreateRequestDTO.getUserId()).isEmpty()) {
            throw new ValidationException("User ID is required and must exist");
        }
        Customer customer = convertCreateRequestDTOtoEntity(customerCreateRequestDTO);
        validateCustomer(customer);
        Customer savedCustomer = customerRepository.save(customer);
        return convertEntityToDTO(savedCustomer);
    }

    @Override
    public CustomerDTO getCustomerById(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("Customer not found with ID: " + customerId));
        return convertEntityToDTO(customer);
    }

    @Override
    public List<CustomerDTO> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(this::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public CustomerDTO updateCustomer(Long customerId, CustomerUpdateRequestDTO customerUpdateRequestDTO,
            UserDetails userDetails) {
        Customer existingCustomer = customerRepository.findById(customerId)
                .orElseThrow(() -> new NoSuchElementException("Customer not found with ID: " + customerId));

        // Get the user ID of the logged-in user
        String username = userDetails.getUsername();
        User loggedInUser = userRepository.findByUsername(username);
        Long loggedInUserId = loggedInUser.getUserId(); // Assuming your User entity has a getUserId() method

        // Get the user ID associated with the customer being updated
        Long customerUserId = existingCustomer.getUserId().getUserId(); // Assuming your Customer entity has a getUserId() method

        // Check if the logged-in user's ID matches the customer's user ID
        if (!loggedInUserId.equals(customerUserId)) {
            throw new IllegalArgumentException("You are not authorized to update this customer's information.");
        }

        if (customerUpdateRequestDTO.getName() != null) {
            existingCustomer.setName(customerUpdateRequestDTO.getName());
        }
        if (customerUpdateRequestDTO.getGender() != null) {
            existingCustomer.setGender(customerUpdateRequestDTO.getGender());
        }
        if (customerUpdateRequestDTO.getContactNumber() != null) {
            existingCustomer.setContactNumber(customerUpdateRequestDTO.getContactNumber());
        }
        if (customerUpdateRequestDTO.getAddress() != null) {
            existingCustomer.setAddress(customerUpdateRequestDTO.getAddress());
        }
        if (customerUpdateRequestDTO.getDateOfBirth() != null) {
            existingCustomer.setDateOfBirth(customerUpdateRequestDTO.getDateOfBirth());
        }
        if (customerUpdateRequestDTO.getAadharNumber() != null) {
            existingCustomer.setAadharNumber(customerUpdateRequestDTO.getAadharNumber());
        }
        if (customerUpdateRequestDTO.getPanNumber() != null) {
            existingCustomer.setPanNumber(customerUpdateRequestDTO.getPanNumber());
        }

        validateCustomer(existingCustomer);
        Customer updatedCustomer = customerRepository.save(existingCustomer);
        return convertEntityToDTO(updatedCustomer);
    }

    @Override
    public void deleteCustomer(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new NoSuchElementException("Customer not found with ID: " + customerId);
        }
        customerRepository.deleteById(customerId);
    }

    @Override
    public CustomerDTO getCustomerByUserId(Long userId) {
        Customer customer = customerRepository.findByUser_UserId(userId);
        if (customer == null) {
            throw new NoSuchElementException("Customer not found for User ID: " + userId);
        }
        return convertEntityToDTO(customer);
    }

    private CustomerDTO convertEntityToDTO(Customer customer) {
        CustomerDTO dto = new CustomerDTO();
        dto.setCustomerId(customer.getCustomerId());
        dto.setName(customer.getName());
        dto.setGender(customer.getGender());
        dto.setContactNumber(customer.getContactNumber());
        dto.setAddress(customer.getAddress());
        dto.setDateOfBirth(customer.getDateOfBirth());
        dto.setAadharNumber(customer.getAadharNumber());
        dto.setPanNumber(customer.getPanNumber());

        if (customer.getUserId() != null) {
            dto.setUserId(customer.getUserId().getUserId());
        }
        return dto;
    }

    private Customer convertCreateRequestDTOtoEntity(CustomerCreateRequestDTO dto) {
        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setGender(dto.getGender());
        customer.setContactNumber(dto.getContactNumber());
        customer.setAddress(dto.getAddress());
        customer.setDateOfBirth(dto.getDateOfBirth());
        customer.setAadharNumber(dto.getAadharNumber());
        customer.setPanNumber(dto.getPanNumber());

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new ValidationException("User not found with ID: " + dto.getUserId()));
        customer.setUserId(user);

        return customer;
    }


}