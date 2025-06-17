package com.hexaware.maverickBank.service.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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

import com.hexaware.maverickBank.dto.BeneficiaryCreateRequestDTO;
import com.hexaware.maverickBank.dto.BeneficiaryDTO;
import com.hexaware.maverickBank.dto.BeneficiaryUpdateRequestDTO;
import com.hexaware.maverickBank.dto.CustomerDTO;
import com.hexaware.maverickBank.entity.Beneficiary;
import com.hexaware.maverickBank.entity.Customer;
import com.hexaware.maverickBank.repository.IBeneficiaryRepository;

import jakarta.validation.ValidationException;

class BeneficiaryServiceImplTest {

    @InjectMocks
    private BeneficiaryServiceImpl beneficiaryService;

    @Mock
    private IBeneficiaryRepository beneficiaryRepository;

    @Mock
    private CustomerServiceImpl customerService;

    private Beneficiary beneficiary;
    private BeneficiaryDTO beneficiaryDTO;
    private BeneficiaryCreateRequestDTO createRequestDTO;
    private BeneficiaryUpdateRequestDTO updateRequestDTO;
    private Customer customer;
    private CustomerDTO customerDTO; // Added CustomerDTO

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        customer = new Customer();
        customer.setCustomerId(1L);
        beneficiary = new Beneficiary();
        beneficiary.setBeneficiaryId(1L);
        beneficiary.setCustomer(customer);
        beneficiary.setBeneficiaryName("Test Beneficiary");
        beneficiary.setAccountNumber("1234567890");
        beneficiary.setBankName("Test Bank");
        beneficiary.setBranchName("Test Branch");
        beneficiary.setIfscCode("TEST1234567");
        beneficiaryDTO = new BeneficiaryDTO();
        beneficiaryDTO.setBeneficiaryId(1L);
        beneficiaryDTO.setCustomerId(1L);
        beneficiaryDTO.setBeneficiaryName("Test Beneficiary");
        beneficiaryDTO.setAccountNumber("1234567890");
        beneficiaryDTO.setBankName("Test Bank");
        beneficiaryDTO.setBranchName("Test Branch");
        beneficiaryDTO.setIfscCode("TEST1234567");
        createRequestDTO = new BeneficiaryCreateRequestDTO();
        createRequestDTO.setCustomerId(1L);
        createRequestDTO.setBeneficiaryName("Test Beneficiary");
        createRequestDTO.setAccountNumber("1234567890");
        createRequestDTO.setBankName("Test Bank");
        createRequestDTO.setBranchName("Test Branch");
        createRequestDTO.setIfscCode("TEST1234567");
        updateRequestDTO = new BeneficiaryUpdateRequestDTO();
        customerDTO = new CustomerDTO(); // Initialize CustomerDTO
        customerDTO.setCustomerId(1L);
    }

    @Test
    void testCreateBeneficiary() {
        when(customerService.getCustomerById(1L)).thenReturn(customerDTO); // Mock customer existence with CustomerDTO
        when(beneficiaryRepository.save(any(Beneficiary.class))).thenReturn(beneficiary);

        BeneficiaryDTO createdBeneficiaryDTO = beneficiaryService.createBeneficiary(createRequestDTO);
        assertNotNull(createdBeneficiaryDTO);
        assertEquals(beneficiaryDTO.getBeneficiaryName(), createdBeneficiaryDTO.getBeneficiaryName());
        verify(beneficiaryRepository, times(1)).save(any(Beneficiary.class));
    }

    @Test
    void testCreateBeneficiary_InvalidCustomerId() {
        createRequestDTO.setCustomerId(null);
        assertThrows(ValidationException.class, () -> beneficiaryService.createBeneficiary(createRequestDTO));
        verify(beneficiaryRepository, never()).save(any());
    }

    @Test
    void testCreateBeneficiary_NonExistingCustomerId() {
        when(customerService.getCustomerById(2L)).thenThrow(NoSuchElementException.class);
        createRequestDTO.setCustomerId(2L);
        assertThrows(NoSuchElementException.class, () -> beneficiaryService.createBeneficiary(createRequestDTO));
        verify(beneficiaryRepository, never()).save(any());
    }

    @Test
    void testCreateBeneficiary_EmptyBeneficiaryName() {
        createRequestDTO.setBeneficiaryName("");
        when(customerService.getCustomerById(1L)).thenReturn(customerDTO);
        assertThrows(ValidationException.class, () -> beneficiaryService.createBeneficiary(createRequestDTO));
        verify(beneficiaryRepository, never()).save(any());
    }

    @Test
    void testCreateBeneficiary_InvalidIfscCode() {
        createRequestDTO.setIfscCode("INVALID");
        when(customerService.getCustomerById(1L)).thenReturn(customerDTO);
        assertThrows(ValidationException.class, () -> beneficiaryService.createBeneficiary(createRequestDTO));
        verify(beneficiaryRepository, never()).save(any());
    }

    @Test
    void testGetBeneficiaryById() {
        when(beneficiaryRepository.findById(1L)).thenReturn(Optional.of(beneficiary));
        BeneficiaryDTO foundBeneficiaryDTO = beneficiaryService.getBeneficiaryById(1L);
        assertNotNull(foundBeneficiaryDTO);
        assertEquals(beneficiaryDTO.getBeneficiaryId(), foundBeneficiaryDTO.getBeneficiaryId());
    }

    @Test
    void testGetBeneficiaryById_NotFound() {
        when(beneficiaryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> beneficiaryService.getBeneficiaryById(1L));
    }

    @Test
    void testGetAllBeneficiaries() {
        when(beneficiaryRepository.findAll()).thenReturn(Collections.singletonList(beneficiary));
        List<BeneficiaryDTO> allBeneficiariesDTO = beneficiaryService.getAllBeneficiaries();
        assertNotNull(allBeneficiariesDTO);
        assertEquals(1, allBeneficiariesDTO.size());
        assertEquals(beneficiaryDTO.getBeneficiaryId(), allBeneficiariesDTO.get(0).getBeneficiaryId());
    }

    @Test
    void testUpdateBeneficiary() {
        updateRequestDTO.setBeneficiaryName("Updated Beneficiary");
        when(beneficiaryRepository.findById(1L)).thenReturn(Optional.of(beneficiary));
        Beneficiary updatedBeneficiary = new Beneficiary();
        BeanUtils.copyProperties(beneficiary, updatedBeneficiary);
        updatedBeneficiary.setBeneficiaryName("Updated Beneficiary");
        when(beneficiaryRepository.save(any(Beneficiary.class))).thenReturn(updatedBeneficiary);

        BeneficiaryDTO updatedBeneficiaryDTO = beneficiaryService.updateBeneficiary(1L, updateRequestDTO);
        assertNotNull(updatedBeneficiaryDTO);
        assertEquals("Updated Beneficiary", updatedBeneficiaryDTO.getBeneficiaryName());
        verify(beneficiaryRepository, times(1)).save(any(Beneficiary.class));
    }

    @Test
    void testUpdateBeneficiary_NotFound() {
        when(beneficiaryRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> beneficiaryService.updateBeneficiary(1L, updateRequestDTO));
    }

    @Test
    void testUpdateBeneficiary_InvalidIfscCode() {
        updateRequestDTO.setIfscCode("INVALID");
        when(beneficiaryRepository.findById(1L)).thenReturn(Optional.of(beneficiary));
        assertThrows(ValidationException.class, () -> beneficiaryService.updateBeneficiary(1L, updateRequestDTO));
        verify(beneficiaryRepository, never()).save(any());
    }

    @Test
    void testDeleteBeneficiary() {
        when(beneficiaryRepository.existsById(1L)).thenReturn(true);
        beneficiaryService.deleteBeneficiary(1L);
        verify(beneficiaryRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBeneficiary_NotFound() {
        when(beneficiaryRepository.existsById(1L)).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> beneficiaryService.deleteBeneficiary(1L));
    }

    @Test
    void testGetBeneficiariesByCustomerId() {
        when(customerService.getCustomerById(1L)).thenReturn(customerDTO); // Mock customer existence with CustomerDTO
        when(beneficiaryRepository.findByCustomer_CustomerId(1L)).thenReturn(Collections.singletonList(beneficiary));
        List<BeneficiaryDTO> beneficiariesDTOList = beneficiaryService.getBeneficiariesByCustomerId(1L);
        assertNotNull(beneficiariesDTOList);
        assertEquals(1, beneficiariesDTOList.size());
        assertEquals(beneficiaryDTO.getBeneficiaryId(), beneficiariesDTOList.get(0).getBeneficiaryId());
    }

    @Test
    void testGetBeneficiariesByCustomerId_CustomerNotFound() {
        when(customerService.getCustomerById(2L)).thenThrow(NoSuchElementException.class);
        assertThrows(NoSuchElementException.class, () -> beneficiaryService.getBeneficiariesByCustomerId(2L));
    }

    @Test
    void testGetBeneficiariesByCustomerId_NoBeneficiariesFound() {
        when(customerService.getCustomerById(1L)).thenReturn(customerDTO); // Mock customer existence with CustomerDTO
        when(beneficiaryRepository.findByCustomer_CustomerId(1L)).thenReturn(Collections.emptyList());
        assertThrows(NoSuchElementException.class, () -> beneficiaryService.getBeneficiariesByCustomerId(1L));
    }
}