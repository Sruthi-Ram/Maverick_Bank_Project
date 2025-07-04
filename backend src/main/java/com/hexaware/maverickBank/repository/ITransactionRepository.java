package com.hexaware.maverickbank.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hexaware.maverickbank.dto.entity.Transaction;
@Repository
public interface ITransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByAccount_AccountIdOrderByTransactionDateDesc(Long accountId);
    List<Transaction> findByAccount_AccountIdAndTransactionDateBetweenOrderByTransactionDateDesc(Long accountId, LocalDateTime startDate, LocalDateTime endDate);
}