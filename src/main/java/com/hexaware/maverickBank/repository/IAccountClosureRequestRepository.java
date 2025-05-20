package com.hexaware.maverickBank.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hexaware.maverickBank.entity.AccountClosureRequest;

@Repository
public interface IAccountClosureRequestRepository {
	List<AccountClosureRequest> findByAccount_AccountId(int accountId);

	List<AccountClosureRequest> findByStatus(String status);
}
