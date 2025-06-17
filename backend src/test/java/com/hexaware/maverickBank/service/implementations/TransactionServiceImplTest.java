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
import java.time.LocalDateTime;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.hexaware.maverickBank.dto.TransactionDTO;
import com.hexaware.maverickBank.entity.Account;
import com.hexaware.maverickBank.entity.Beneficiary;
import com.hexaware.maverickBank.entity.Transaction;
import com.hexaware.maverickBank.repository.IAccountRepository;
import com.hexaware.maverickBank.repository.IBeneficiaryRepository;
import com.hexaware.maverickBank.repository.ITransactionRepository;

class TransactionServiceImplTest {

    @InjectMocks
    private TransactionServiceImpl transactionService;

    @Mock
    private ITransactionRepository transactionRepository;

    @Mock
    private IAccountRepository accountRepository;

    @Mock
    private IBeneficiaryRepository beneficiaryRepository;

    private Transaction transaction;
    private TransactionDTO transactionDTO;
    private Account account;
    private Beneficiary beneficiary;
    private LocalDateTime now;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        now = LocalDateTime.now();
        account = new Account();
        account.setAccountId(100L);
        beneficiary = new Beneficiary();
        beneficiary.setBeneficiaryId(200L);
        transaction = new Transaction();
        transaction.setTransactionId(1L);
        transaction.setAccount(account);
        transaction.setTransactionType("DEPOSIT");
        transaction.setAmount(BigDecimal.valueOf(100));
        transaction.setTransactionDate(now);
        transaction.setDescription("Test Deposit");
        transaction.setBeneficiary(beneficiary);
        transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionId(1L);
        transactionDTO.setAccountId(100L);
        transactionDTO.setTransactionType("DEPOSIT");
        transactionDTO.setAmount(BigDecimal.valueOf(100));
        transactionDTO.setTransactionDate(now);
        transactionDTO.setDescription("Test Deposit");
        transactionDTO.setBeneficiaryId(200L);
    }

    @Test
    void testCreateTransaction() {
        when(accountRepository.findById(100L)).thenReturn(Optional.of(account));
        when(beneficiaryRepository.findById(200L)).thenReturn(Optional.of(beneficiary));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);

        TransactionDTO createdTransactionDTO = transactionService.createTransaction(transactionDTO);
        assertNotNull(createdTransactionDTO);
        assertEquals(transactionDTO.getTransactionId(), createdTransactionDTO.getTransactionId());
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void testCreateTransaction_AccountNotFound() {
        when(accountRepository.findById(100L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> transactionService.createTransaction(transactionDTO));
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void testCreateTransaction_BeneficiaryNotFound() {
        when(accountRepository.findById(100L)).thenReturn(Optional.of(account));
        when(beneficiaryRepository.findById(200L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> transactionService.createTransaction(transactionDTO));
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void testGetTransactionById() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.of(transaction));
        TransactionDTO foundTransactionDTO = transactionService.getTransactionById(1L);
        assertNotNull(foundTransactionDTO);
        assertEquals(transactionDTO.getTransactionId(), foundTransactionDTO.getTransactionId());
    }

    @Test
    void testGetTransactionById_NotFound() {
        when(transactionRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> transactionService.getTransactionById(1L));
    }

    @Test
    void testDeleteTransaction() {
        when(transactionRepository.existsById(1L)).thenReturn(true);
        transactionService.deleteTransaction(1L);
        verify(transactionRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteTransaction_NotFound() {
        when(transactionRepository.existsById(1L)).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> transactionService.deleteTransaction(1L));
        verify(transactionRepository, never()).deleteById(any());
    }
}