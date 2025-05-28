package com.hexaware.maverickBank.service.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hexaware.maverickBank.dto.BankBranchCreateRequestDTO;
import com.hexaware.maverickBank.dto.BankBranchDTO;
import com.hexaware.maverickBank.dto.BankBranchUpdateRequestDTO;
import com.hexaware.maverickBank.entity.BankBranch;
import com.hexaware.maverickBank.repository.IBankBranchRepository;

import jakarta.validation.ValidationException;

class BankBranchServiceImplTest {

    @Mock
    private IBankBranchRepository bankBranchRepository;

    @InjectMocks
    private BankBranchServiceImpl bankBranchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateBankBranch_Success() {
        BankBranchCreateRequestDTO requestDTO = new BankBranchCreateRequestDTO("Test Branch", "123 Main St", "MAVR");
        BankBranch savedBranch = new BankBranch(1L, "Test Branch", "123 Main St", "MAVR");
        when(bankBranchRepository.findByName("Test Branch")).thenReturn(null);
        when(bankBranchRepository.findByIfscPrefix("MAVR")).thenReturn(null);
        when(bankBranchRepository.save(any(BankBranch.class))).thenReturn(savedBranch);

        BankBranchDTO branchDTO = bankBranchService.createBankBranch(requestDTO);
        assertNotNull(branchDTO);
        assertEquals(1L, branchDTO.getBranchId());
        assertEquals("Test Branch", branchDTO.getName());
        assertEquals("123 Main St", branchDTO.getAddress());
        assertEquals("MAVR", branchDTO.getIfscPrefix());
    }

    @Test
    void testCreateBankBranch_ValidationException_EmptyName() {
        BankBranchCreateRequestDTO requestDTO = new BankBranchCreateRequestDTO("", "123 Main St", "MAVR");
        assertThrows(ValidationException.class, () -> bankBranchService.createBankBranch(requestDTO));
    }

    @Test
    void testCreateBankBranch_ValidationException_EmptyAddress() {
        BankBranchCreateRequestDTO requestDTO = new BankBranchCreateRequestDTO("Test Branch", "", "MAVR");
        assertThrows(ValidationException.class, () -> bankBranchService.createBankBranch(requestDTO));
    }

    @Test
    void testCreateBankBranch_ValidationException_InvalidIfscPrefix() {
        BankBranchCreateRequestDTO requestDTO = new BankBranchCreateRequestDTO("Test Branch", "123 Main St", "MAV12");
        assertThrows(ValidationException.class, () -> bankBranchService.createBankBranch(requestDTO));
    }

    @Test
    void testCreateBankBranch_ValidationException_ExistingName() {
        BankBranchCreateRequestDTO requestDTO = new BankBranchCreateRequestDTO("Test Branch", "123 Main St", "MAVR");
        when(bankBranchRepository.findByName("Test Branch")).thenReturn(new BankBranch());
        assertThrows(ValidationException.class, () -> bankBranchService.createBankBranch(requestDTO));
    }

    @Test
    void testCreateBankBranch_ValidationException_ExistingIfscPrefix() {
        BankBranchCreateRequestDTO requestDTO = new BankBranchCreateRequestDTO("Test Branch", "123 Main St", "MAVR");
        when(bankBranchRepository.findByIfscPrefix("MAVR")).thenReturn(new BankBranch());
        assertThrows(ValidationException.class, () -> bankBranchService.createBankBranch(requestDTO));
    }

    @Test
    void testGetBankBranchById_Success() {
        BankBranch branch = new BankBranch(1L, "Test Branch", "123 Main St", "MAVR");
        when(bankBranchRepository.findById(1L)).thenReturn(Optional.of(branch));

        BankBranchDTO branchDTO = bankBranchService.getBankBranchById(1L);
        assertNotNull(branchDTO);
        assertEquals(1L, branchDTO.getBranchId());
        assertEquals("Test Branch", branchDTO.getName());
    }

    @Test
    void testGetBankBranchById_NotFound() {
        when(bankBranchRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> bankBranchService.getBankBranchById(1L));
    }

    @Test
    void testGetAllBankBranches() {
        List<BankBranch> branches = new ArrayList<>();
        branches.add(new BankBranch(1L, "Branch 1", "Address 1", "IFSC1"));
        branches.add(new BankBranch(2L, "Branch 2", "Address 2", "IFSC2"));
        when(bankBranchRepository.findAll()).thenReturn(branches);

        List<BankBranchDTO> branchDTOs = bankBranchService.getAllBankBranches();
        assertNotNull(branchDTOs);
        assertEquals(2, branchDTOs.size());
    }

    @Test
    void testUpdateBankBranch_Success() {
        BankBranchUpdateRequestDTO requestDTO = new BankBranchUpdateRequestDTO("Updated Branch", "Updated Address", "IFCU");
        BankBranch existingBranch = new BankBranch(1L, "Test Branch", "123 Main St", "MAVR");
        BankBranch updatedBranch = new BankBranch(1L, "Updated Branch", "Updated Address", "IFCU");
        when(bankBranchRepository.findById(1L)).thenReturn(Optional.of(existingBranch));
        when(bankBranchRepository.findByName("Updated Branch")).thenReturn(null);
        when(bankBranchRepository.findByIfscPrefix("IFCU")).thenReturn(null);
        when(bankBranchRepository.save(any(BankBranch.class))).thenReturn(updatedBranch);

        BankBranchDTO branchDTO = bankBranchService.updateBankBranch(1L, requestDTO);
        assertNotNull(branchDTO);
        assertEquals("Updated Branch", branchDTO.getName());
        assertEquals("IFCU", branchDTO.getIfscPrefix());
    }

    @Test
    void testUpdateBankBranch_NotFound() {
        BankBranchUpdateRequestDTO requestDTO = new BankBranchUpdateRequestDTO("Updated Branch", "Updated Address", "IFCU");
        when(bankBranchRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> bankBranchService.updateBankBranch(1L, requestDTO));
    }

    @Test
    void testUpdateBankBranch_ValidationException_ExistingName() {
        BankBranchUpdateRequestDTO requestDTO = new BankBranchUpdateRequestDTO("Existing Branch", "Updated Address", "IFCU");
        BankBranch existingBranch = new BankBranch(1L, "Test Branch", "123 Main St", "MAVR");
        when(bankBranchRepository.findById(1L)).thenReturn(Optional.of(existingBranch));
        when(bankBranchRepository.findByName("Existing Branch")).thenReturn(new BankBranch());
        assertThrows(ValidationException.class, () -> bankBranchService.updateBankBranch(1L, requestDTO));
    }

    @Test
    void testUpdateBankBranch_ValidationException_ExistingIfscPrefix() {
        BankBranchUpdateRequestDTO requestDTO = new BankBranchUpdateRequestDTO("Updated Branch", "Updated Address", "EXST");
        BankBranch existingBranch = new BankBranch(1L, "Test Branch", "123 Main St", "MAVR");
        when(bankBranchRepository.findById(1L)).thenReturn(Optional.of(existingBranch));
        when(bankBranchRepository.findByIfscPrefix("EXST")).thenReturn(new BankBranch());
        assertThrows(ValidationException.class, () -> bankBranchService.updateBankBranch(1L, requestDTO));
    }

    @Test
    void testDeleteBankBranch_Success() {
        when(bankBranchRepository.existsById(1L)).thenReturn(true);
        bankBranchService.deleteBankBranch(1L);
        verify(bankBranchRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteBankBranch_NotFound() {
        when(bankBranchRepository.existsById(1L)).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> bankBranchService.deleteBankBranch(1L));
        verify(bankBranchRepository, never()).deleteById(anyLong());
    }

    @Test
    void testGetBankBranchByName_Success() {
        BankBranch branch = new BankBranch(1L, "Test Branch", "123 Main St", "MAVR");
        when(bankBranchRepository.findByName("Test Branch")).thenReturn(branch);

        BankBranchDTO branchDTO = bankBranchService.getBankBranchByName("Test Branch");
        assertNotNull(branchDTO);
        assertEquals("Test Branch", branchDTO.getName());
    }

    @Test
    void testGetBankBranchByName_NotFound() {
        when(bankBranchRepository.findByName("NonExisting Branch")).thenReturn(null);
        assertThrows(NoSuchElementException.class, () -> bankBranchService.getBankBranchByName("NonExisting Branch"));
    }

    @Test
    void testGetBankBranchByIfscPrefix_Success() {
        BankBranch branch = new BankBranch(1L, "Test Branch", "123 Main St", "MAVR");
        when(bankBranchRepository.findByIfscPrefix("MAVR")).thenReturn(branch);

        BankBranchDTO branchDTO = bankBranchService.getBankBranchByIfscPrefix("MAVR");
        assertNotNull(branchDTO);
        assertEquals("MAVR", branchDTO.getIfscPrefix());
    }

    @Test
    void testGetBankBranchByIfscPrefix_NotFound() {
        when(bankBranchRepository.findByIfscPrefix("XXXX")).thenReturn(null);
        assertThrows(NoSuchElementException.class, () -> bankBranchService.getBankBranchByIfscPrefix("XXXX"));
    }
}