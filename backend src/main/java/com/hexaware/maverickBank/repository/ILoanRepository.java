package com.hexaware.maverickbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hexaware.maverickbank.dto.entity.Loan;
@Repository
public interface ILoanRepository extends JpaRepository<Loan, Long> {
}