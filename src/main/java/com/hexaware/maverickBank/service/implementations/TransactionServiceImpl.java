package com.hexaware.maverickBank.service.implementations;

import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexaware.maverickBank.entity.Transaction;
import com.hexaware.maverickBank.repository.ITransactionRepository;
import com.hexaware.maverickBank.service.interfaces.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    @Autowired
    private ITransactionRepository transactionRepository;

    @Override
    public Transaction createTransaction(Transaction transaction) {
        return transactionRepository.save(transaction);
    }

    @Override
    public Transaction getTransactionById(Long transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new NoSuchElementException("Transaction not found with ID: " + transactionId));
    }

    @Override
    public void deleteTransaction(Long transactionId) {
        if (!transactionRepository.existsById(transactionId)) {
            throw new NoSuchElementException("Transaction not found with ID: " + transactionId);
        }
        transactionRepository.deleteById(transactionId);
    }
}