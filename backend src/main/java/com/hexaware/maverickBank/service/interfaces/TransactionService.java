package com.hexaware.maverickbank.service.interfaces;

import java.util.List;

import com.hexaware.maverickbank.dto.TransactionDTO;

public interface TransactionService {
    TransactionDTO createTransaction(TransactionDTO transactionDTO);
    TransactionDTO getTransactionById(Long transactionId);
    void deleteTransaction(Long transactionId);
    List<TransactionDTO> getAllTransactions();

}