package com.hexaware.maverickBank.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.hexaware.maverickBank.entity.Account;

@Repository
public interface IAccountRepository extends JpaRepository<Account, Integer> {
	List<Account> findByCustomer_CustomerId(int customerId);

	Account findByAccountNumber(String accountNumber);

	List<Account> findByAccountType(String accountType);

	@Query(value = "SELECT a.* FROM Accounts a JOIN Transactions t ON a.account_id = t.account_id WHERE a.customer_id = :customerId ORDER BY t.transaction_date DESC LIMIT 10", nativeQuery = true)
	List<Account> findTop10AccountsByCustomerIdOrderByLastTransactionDateDesc(@Param("customerId") int customerId);

	@Query("SELECT t FROM Transaction t WHERE t.account = :account AND t.transactionDate >= :startDate")
	List<com.hexaware.maverickBank.entity.Transaction> findTransactionsForAccountSince(
			@Param("account") Account account, @Param("startDate") LocalDateTime startDate);
}
