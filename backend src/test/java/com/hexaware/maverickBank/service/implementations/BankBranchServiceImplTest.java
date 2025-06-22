package com.hexaware.maverickbank.service.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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

import com.hexaware.maverickbank.dto.BankBranchCreateRequestDTO;
import com.hexaware.maverickbank.dto.BankBranchDTO;
import com.hexaware.maverickbank.dto.BankBranchUpdateRequestDTO;
import com.hexaware.maverickbank.dto.entity.BankBranch;
import com.hexaware.maverickbank.repository.IBankBranchRepository;

import jakarta.validation.ValidationException;

class BankBranchServiceImplTest {

	@InjectMocks
	private BankBranchServiceImpl bankBranchService;

	@Mock
	private IBankBranchRepository bankBranchRepository;

	private BankBranch branch;
	private BankBranchDTO branchDTO;
	private BankBranchCreateRequestDTO createRequestDTO;
	private BankBranchUpdateRequestDTO updateRequestDTO;

	@BeforeEach
	void setUp() {
		MockitoAnnotations.openMocks(this);
		branch = new BankBranch(1L, "Test Branch", "123 Test Address", "TEST");
		branchDTO = new BankBranchDTO(1L, "Test Branch", "123 Test Address", "TEST");
		createRequestDTO = new BankBranchCreateRequestDTO("Test Branch", "123 Test Address", "TEST");
		updateRequestDTO = new BankBranchUpdateRequestDTO();
	}

	@Test
	void testCreateBankBranch() {
		when(bankBranchRepository.findByName(anyString())).thenReturn(null);
		when(bankBranchRepository.findByIfscPrefix(anyString())).thenReturn(null);
		when(bankBranchRepository.save(any(BankBranch.class))).thenReturn(branch);

		BankBranchDTO createdBranchDTO = bankBranchService.createBankBranch(createRequestDTO);
		assertNotNull(createdBranchDTO);
		assertEquals(branchDTO.getName(), createdBranchDTO.getName());
		assertEquals(branchDTO.getAddress(), createdBranchDTO.getAddress());
		assertEquals(branchDTO.getIfscPrefix(), createdBranchDTO.getIfscPrefix());
		verify(bankBranchRepository, times(1)).save(any(BankBranch.class));
	}

	@Test
	void testCreateBankBranch_EmptyName() {
		createRequestDTO.setName("");
		assertThrows(ValidationException.class, () -> bankBranchService.createBankBranch(createRequestDTO));
	}

	@Test
	void testCreateBankBranch_EmptyAddress() {
		createRequestDTO.setAddress("");
		assertThrows(ValidationException.class, () -> bankBranchService.createBankBranch(createRequestDTO));
	}

	@Test
	void testCreateBankBranch_InvalidIfscPrefix() {
		createRequestDTO.setIfscPrefix("TEST1");
		assertThrows(ValidationException.class, () -> bankBranchService.createBankBranch(createRequestDTO));
	}

	@Test
	void testCreateBankBranch_ExistingName() {
		when(bankBranchRepository.findByName(anyString())).thenReturn(branch);
		assertThrows(ValidationException.class, () -> bankBranchService.createBankBranch(createRequestDTO));
	}

	@Test
	void testCreateBankBranch_ExistingIfscPrefix() {
		when(bankBranchRepository.findByIfscPrefix(anyString())).thenReturn(branch);
		assertThrows(ValidationException.class, () -> bankBranchService.createBankBranch(createRequestDTO));
	}

	@Test
	void testGetBankBranchById() {
		when(bankBranchRepository.findById(1L)).thenReturn(Optional.of(branch));
		BankBranchDTO foundBranchDTO = bankBranchService.getBankBranchById(1L);
		assertNotNull(foundBranchDTO);
		assertEquals(branchDTO.getBranchId(), foundBranchDTO.getBranchId());
	}

	@Test
	void testGetBankBranchById_NotFound() {
		when(bankBranchRepository.findById(1L)).thenReturn(Optional.empty());
		assertThrows(NoSuchElementException.class, () -> bankBranchService.getBankBranchById(1L));
	}

	@Test
	void testGetAllBankBranches() {
		when(bankBranchRepository.findAll()).thenReturn(Collections.singletonList(branch));
		List<BankBranchDTO> allBranchesDTO = bankBranchService.getAllBankBranches();
		assertNotNull(allBranchesDTO);
		assertEquals(1, allBranchesDTO.size());
		assertEquals(branchDTO.getBranchId(), allBranchesDTO.get(0).getBranchId());
	}

	@Test
	void testUpdateBankBranch() {
		updateRequestDTO.setName("Updated Branch");
		updateRequestDTO.setAddress("456 Updated Address");
		updateRequestDTO.setIfscPrefix("UPDT");
		BankBranch updatedBranch = new BankBranch(1L, "Updated Branch", "456 Updated Address", "UPDT");
		when(bankBranchRepository.findById(1L)).thenReturn(Optional.of(branch));
		when(bankBranchRepository.findByName("Updated Branch")).thenReturn(null);
		when(bankBranchRepository.findByIfscPrefix("UPDT")).thenReturn(null);
		when(bankBranchRepository.save(any(BankBranch.class))).thenReturn(updatedBranch);

		BankBranchDTO updatedBranchDTO = bankBranchService.updateBankBranch(1L, updateRequestDTO);
		assertNotNull(updatedBranchDTO);
		assertEquals("Updated Branch", updatedBranchDTO.getName());
		assertEquals("456 Updated Address", updatedBranchDTO.getAddress());
		assertEquals("UPDT", updatedBranchDTO.getIfscPrefix());
		verify(bankBranchRepository, times(1)).save(any(BankBranch.class));
	}

	@Test
	void testUpdateBankBranch_NotFound() {
		when(bankBranchRepository.findById(1L)).thenReturn(Optional.empty());
		assertThrows(NoSuchElementException.class, () -> bankBranchService.updateBankBranch(1L, updateRequestDTO));
	}

	@Test
	void testUpdateBankBranch_ExistingName() {
		updateRequestDTO.setName("Existing Branch");
		when(bankBranchRepository.findById(1L)).thenReturn(Optional.of(branch));
		when(bankBranchRepository.findByName("Existing Branch")).thenReturn(new BankBranch(2L, "Existing Branch", "Address", "EXST"));
		assertThrows(ValidationException.class, () -> bankBranchService.updateBankBranch(1L, updateRequestDTO));
	}

	@Test
	void testUpdateBankBranch_ExistingIfscPrefix() {
		updateRequestDTO.setIfscPrefix("EXST");
		when(bankBranchRepository.findById(1L)).thenReturn(Optional.of(branch));
		when(bankBranchRepository.findByIfscPrefix("EXST")).thenReturn(new BankBranch(2L, "Name", "Address", "EXST"));
		assertThrows(ValidationException.class, () -> bankBranchService.updateBankBranch(1L, updateRequestDTO));
	}

	@Test
	void testDeleteBankBranch() {
		when(bankBranchRepository.existsById(1L)).thenReturn(true);
		bankBranchService.deleteBankBranch(1L);
		verify(bankBranchRepository, times(1)).deleteById(1L);
	}

	@Test
	void testDeleteBankBranch_NotFound() {
		when(bankBranchRepository.existsById(1L)).thenReturn(false);
		assertThrows(NoSuchElementException.class, () -> bankBranchService.deleteBankBranch(1L));
	}

	@Test
	void testGetBankBranchByName() {
		when(bankBranchRepository.findByName("Test Branch")).thenReturn(branch);
		BankBranchDTO foundBranchDTO = bankBranchService.getBankBranchByName("Test Branch");
		assertNotNull(foundBranchDTO);
		assertEquals(branchDTO.getBranchId(), foundBranchDTO.getBranchId());
	}

	@Test
	void testGetBankBranchByName_NotFound() {
		when(bankBranchRepository.findByName("NonExisting")).thenReturn(null);
		assertThrows(NoSuchElementException.class, () -> bankBranchService.getBankBranchByName("NonExisting"));
	}

	@Test
	void testGetBankBranchByIfscPrefix() {
		when(bankBranchRepository.findByIfscPrefix("TEST")).thenReturn(branch);
		BankBranchDTO foundBranchDTO = bankBranchService.getBankBranchByIfscPrefix("TEST");
		assertNotNull(foundBranchDTO);
		assertEquals(branchDTO.getBranchId(), foundBranchDTO.getBranchId());
	}

	@Test
	void testGetBankBranchByIfscPrefix_NotFound() {
		when(bankBranchRepository.findByIfscPrefix("XXXX")).thenReturn(null);
		assertThrows(NoSuchElementException.class, () -> bankBranchService.getBankBranchByIfscPrefix("XXXX"));
	}
}