package com.hexaware.maverickBank.service.interfaces;

import com.hexaware.maverickBank.entity.Transaction;

public interface TransactionService {
    Transaction createTransaction(Transaction transaction);
    Transaction getTransactionById(Long transactionId);
    void deleteTransaction(Long transactionId);
}