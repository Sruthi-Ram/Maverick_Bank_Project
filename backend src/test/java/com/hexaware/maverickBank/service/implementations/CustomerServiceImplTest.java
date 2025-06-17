package com.hexaware.maverickBank.service.implementations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.userdetails.UserDetails;

import com.hexaware.maverickBank.dto.CustomerCreateRequestDTO;
import com.hexaware.maverickBank.dto.CustomerDTO;
import com.hexaware.maverickBank.dto.CustomerUpdateRequestDTO;
import com.hexaware.maverickBank.entity.Customer;
import com.hexaware.maverickBank.entity.User;
import com.hexaware.maverickBank.repository.ICustomerRepository;
import com.hexaware.maverickBank.repository.IUserRepository;

import jakarta.validation.ValidationException;

class CustomerServiceImplTest {

    @InjectMocks
    private CustomerServiceImpl customerService;

    @Mock
    private ICustomerRepository customerRepository;

    @Mock
    private IUserRepository userRepository;

    private Customer customer;
    private CustomerDTO customerDTO;
    private CustomerCreateRequestDTO createRequestDTO;
    private CustomerUpdateRequestDTO updateRequestDTO;
    private User user;
    private UserDetails userDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUserId(1L);
        user.setUsername("testUser");
        customer = new Customer();
        customer.setCustomerId(1L);
        customer.setUserId(user);
        customer.setName("Test Customer");
        customer.setDateOfBirth(LocalDate.now().minusYears(20));
        customer.setAddress("123 Test Address");
        customer.setContactNumber("1234567890");
        customerDTO = new CustomerDTO();
        customerDTO.setCustomerId(1L);
        customerDTO.setUserId(1L);
        customerDTO.setName("Test Customer");
        customerDTO.setDateOfBirth(LocalDate.now().minusYears(20));
        customerDTO.setAddress("123 Test Address");
        customerDTO.setContactNumber("1234567890");
        createRequestDTO = new CustomerCreateRequestDTO();
        createRequestDTO.setUserId(1L);
        createRequestDTO.setName("Test Customer");
        createRequestDTO.setDateOfBirth(LocalDate.now().minusYears(20));
        createRequestDTO.setAddress("123 Test Address");
        createRequestDTO.setContactNumber("1234567890");
        updateRequestDTO = new CustomerUpdateRequestDTO();
        userDetails = org.springframework.security.core.userdetails.User.withUsername("testUser").password("password").roles("CUSTOMER").build();
    }

    @Test
    void testCreateCustomer() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerDTO createdCustomerDTO = customerService.createCustomer(createRequestDTO);
        assertNotNull(createdCustomerDTO);
        assertEquals(customerDTO.getCustomerId(), createdCustomerDTO.getCustomerId());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testCreateCustomer_InvalidUserId() {
        createRequestDTO.setUserId(null);
        assertThrows(ValidationException.class, () -> customerService.createCustomer(createRequestDTO));
        verify(customerRepository, never()).save(any());
    }

    @Test
    void testCreateCustomer_NonExistingUserId() {
        createRequestDTO.setUserId(2L);
        when(userRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ValidationException.class, () -> customerService.createCustomer(createRequestDTO));
        verify(customerRepository, never()).save(any());
    }

    @Test
    void testCreateCustomer_EmptyName() {
        createRequestDTO.setName("");
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        assertThrows(ValidationException.class, () -> customerService.createCustomer(createRequestDTO));
        verify(customerRepository, never()).save(any());
    }

    @Test
    void testGetCustomerById() {
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        CustomerDTO foundCustomerDTO = customerService.getCustomerById(1L);
        assertNotNull(foundCustomerDTO);
        assertEquals(customerDTO.getCustomerId(), foundCustomerDTO.getCustomerId());
    }

    @Test
    void testGetCustomerById_NotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> customerService.getCustomerById(1L));
    }

    @Test
    void testGetAllCustomers() {
        when(customerRepository.findAll()).thenReturn(Collections.singletonList(customer));
        List<CustomerDTO> allCustomersDTO = customerService.getAllCustomers();
        assertNotNull(allCustomersDTO);
        assertEquals(1, allCustomersDTO.size());
        assertEquals(customerDTO.getCustomerId(), allCustomersDTO.get(0).getCustomerId());
    }

    @Test
    void testUpdateCustomer() {
        updateRequestDTO.setName("Updated Customer");
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(userRepository.findByUsername("testUser")).thenReturn(user);
        Customer updatedCustomer = new Customer();
        BeanUtils.copyProperties(customer, updatedCustomer);
        updatedCustomer.setName("Updated Customer");
        when(customerRepository.save(any(Customer.class))).thenReturn(updatedCustomer);

        CustomerDTO updatedCustomerDTO = customerService.updateCustomer(1L, updateRequestDTO, userDetails);
        assertNotNull(updatedCustomerDTO);
        assertEquals("Updated Customer", updatedCustomerDTO.getName());
        verify(customerRepository, times(1)).save(any(Customer.class));
    }

    @Test
    void testUpdateCustomer_NotFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> customerService.updateCustomer(1L, updateRequestDTO, userDetails));
    }

    @Test
    void testUpdateCustomer_Unauthorized() {
        UserDetails anotherUser = org.springframework.security.core.userdetails.User.withUsername("anotherUser").password("password").roles("CUSTOMER").build();
        User anotherUserEntity = new User();
        anotherUserEntity.setUserId(2L);
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(userRepository.findByUsername("anotherUser")).thenReturn(anotherUserEntity);
        assertThrows(IllegalArgumentException.class, () -> customerService.updateCustomer(1L, updateRequestDTO, anotherUser));
        verify(customerRepository, never()).save(any());
    }

    @Test
    void testDeleteCustomer() {
        when(customerRepository.existsById(1L)).thenReturn(true);
        customerService.deleteCustomer(1L);
        verify(customerRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteCustomer_NotFound() {
        when(customerRepository.existsById(1L)).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> customerService.deleteCustomer(1L));
    }

    @Test
    void testGetCustomerByUserId() {
        when(customerRepository.findByUser_UserId(1L)).thenReturn(customer);
        CustomerDTO foundCustomerDTO = customerService.getCustomerByUserId(1L);
        assertNotNull(foundCustomerDTO);
        assertEquals(customerDTO.getCustomerId(), foundCustomerDTO.getCustomerId());
    }

    @Test
    void testGetCustomerByUserId_NotFound() {
        when(customerRepository.findByUser_UserId(1L)).thenReturn(null);
        assertThrows(NoSuchElementException.class, () -> customerService.getCustomerByUserId(1L));
    }
}