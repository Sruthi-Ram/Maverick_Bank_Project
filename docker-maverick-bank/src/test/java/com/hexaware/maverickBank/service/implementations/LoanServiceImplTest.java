package com.hexaware.maverickBank.service.implementations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hexaware.maverickBank.dto.LoanCreateRequestDTO;
import com.hexaware.maverickBank.dto.LoanDTO;
import com.hexaware.maverickBank.dto.LoanUpdateRequestDTO;
import com.hexaware.maverickBank.entity.Loan;
import com.hexaware.maverickBank.repository.ILoanRepository;

import jakarta.validation.ValidationException;

class LoanServiceImplTest {

    @InjectMocks
    private LoanServiceImpl loanService;

    @Mock
    private ILoanRepository loanRepository;

    private Loan loan;
    private LoanDTO loanDTO;
    private LoanCreateRequestDTO createRequestDTO;
    private LoanUpdateRequestDTO updateRequestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loan = new Loan(1L, "Home Loan", BigDecimal.valueOf(100000), BigDecimal.valueOf(0.08), 120, BigDecimal.ZERO, "Active", null);
        loanDTO = new LoanDTO(1L, "Home Loan", BigDecimal.valueOf(100000), BigDecimal.valueOf(0.08), 120, BigDecimal.ZERO, "Active");
        createRequestDTO = new LoanCreateRequestDTO("Home Loan", BigDecimal.valueOf(100000), BigDecimal.valueOf(0.08), 120);
        updateRequestDTO = new LoanUpdateRequestDTO();
    }

    @Test
    void testCreateLoan() {
        when(loanRepository.save(any(Loan.class))).thenReturn(loan);
        LoanDTO createdLoanDTO = loanService.createLoan(createRequestDTO);
        assertNotNull(createdLoanDTO);
        assertEquals(loanDTO.getLoanId(), createdLoanDTO.getLoanId());
        assertEquals(loanDTO.getLoanType(), createdLoanDTO.getLoanType());
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    void testCreateLoan_EmptyLoanType() {
        createRequestDTO.setLoanType("");
        assertThrows(ValidationException.class, () -> loanService.createLoan(createRequestDTO));
        verify(loanRepository, never()).save(any());
    }

    @Test
    void testCreateLoan_NegativePrincipalAmount() {
        createRequestDTO.setPrincipalAmount(BigDecimal.valueOf(-1000));
        assertThrows(ValidationException.class, () -> loanService.createLoan(createRequestDTO));
        verify(loanRepository, never()).save(any());
    }

    @Test
    void testGetLoanById() {
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        LoanDTO foundLoanDTO = loanService.getLoanById(1L);
        assertNotNull(foundLoanDTO);
        assertEquals(loanDTO.getLoanId(), foundLoanDTO.getLoanId());
    }

    @Test
    void testGetLoanById_NotFound() {
        when(loanRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> loanService.getLoanById(1L));
    }

    @Test
    void testGetAllLoans() {
        when(loanRepository.findAll()).thenReturn(Collections.singletonList(loan));
        List<LoanDTO> allLoansDTO = loanService.getAllLoans();
        assertNotNull(allLoansDTO);
        assertEquals(1, allLoansDTO.size());
        assertEquals(loanDTO.getLoanId(), allLoansDTO.get(0).getLoanId());
    }

    @Test
    void testUpdateLoan() {
        updateRequestDTO.setStatus("Closed");
        when(loanRepository.findById(1L)).thenReturn(Optional.of(loan));
        Loan updatedLoan = new Loan(1L, "Home Loan", BigDecimal.valueOf(100000), BigDecimal.valueOf(0.08), 120, BigDecimal.ZERO, "Closed", new ArrayList<>());
        when(loanRepository.save(any(Loan.class))).thenReturn(updatedLoan);
        LoanDTO updatedLoanDTO = loanService.updateLoan(1L, updateRequestDTO);
        assertNotNull(updatedLoanDTO);
        assertEquals("Closed", updatedLoanDTO.getStatus());
        verify(loanRepository, times(1)).save(any(Loan.class));
    }

    @Test
    void testUpdateLoan_NotFound() {
        when(loanRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> loanService.updateLoan(1L, updateRequestDTO));
        verify(loanRepository, never()).save(any());
    }

    @Test
    void testDeleteLoan() {
        when(loanRepository.existsById(1L)).thenReturn(true);
        loanService.deleteLoan(1L);
        verify(loanRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteLoan_NotFound() {
        when(loanRepository.existsById(1L)).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> loanService.deleteLoan(1L));
        verify(loanRepository, never()).deleteById(any());
    }
}