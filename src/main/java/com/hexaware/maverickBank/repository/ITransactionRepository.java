package com.hexaware.maverickBank.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hexaware.maverickBank.entity.Account;
import com.hexaware.maverickBank.entity.Transaction;

@Repository
public interface ITransactionRepository extends JpaRepository<Transaction, Integer> {
	@Query("SELECT t FROM Transaction t WHERE t.account.accountId = :accountId ORDER BY t.transactionDate DESC")
	List<Transaction> findTop10ByAccount_AccountIdOrderByTransactionDateDesc(@Param("accountId") int accountId);

	@Query("SELECT COUNT(t) FROM Transaction t WHERE t.account.accountId = :accountId AND t.transactionType = 'deposit'")
	long countInboundTransactionsByAccountId(@Param("accountId") int accountId);

	@Query("SELECT COUNT(t) FROM Transaction t WHERE t.account.accountId = :accountId AND (t.transactionType = 'withdrawal' OR t.transactionType = 'transfer')")
	long countOutboundTransactionsByAccountId(@Param("accountId") int accountId);

	List<Transaction> findByAccountAndTransactionDateGreaterThanEqual(Account account, LocalDateTime startDate);
}
