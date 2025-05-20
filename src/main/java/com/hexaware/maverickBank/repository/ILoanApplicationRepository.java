package com.hexaware.maverickBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hexaware.maverickBank.entity.LoanApplication;

@Repository
public interface ILoanApplicationRepository extends JpaRepository<LoanApplication, Integer> {
	
}
