package com.hexaware.maverickBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hexaware.maverickBank.entity.AccountClosureRequest;

@Repository
public interface IAccountClosureRequestRepository extends JpaRepository<AccountClosureRequest, Integer>{
	
}
