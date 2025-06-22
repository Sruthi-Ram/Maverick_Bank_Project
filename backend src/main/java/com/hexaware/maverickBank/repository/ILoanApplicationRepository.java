package com.hexaware.maverickbank.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hexaware.maverickbank.dto.entity.LoanApplication;
@Repository
public interface ILoanApplicationRepository extends JpaRepository<LoanApplication, Long> {
    List<LoanApplication> findByCustomer_CustomerId(Long customerId);
}