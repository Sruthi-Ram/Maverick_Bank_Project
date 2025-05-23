package com.hexaware.maverickBank.service.interfaces;

import com.hexaware.maverickBank.dto.TransactionDTO;

public interface TransactionService {
    TransactionDTO createTransaction(TransactionDTO transactionDTO);
    TransactionDTO getTransactionById(Long transactionId);
    void deleteTransaction(Long transactionId);
}