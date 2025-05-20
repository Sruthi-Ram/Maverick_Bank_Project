package com.hexaware.maverickBank.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hexaware.maverickBank.entity.Loan;

@Repository
public interface ILoanRepository extends JpaRepository<Loan, Integer> {
	Loan findByLoanName(String loanName);

	List<Loan> findByInterestRate(BigDecimal interestRate);

	List<Loan> findByInterestRateLessThanEqual(BigDecimal interestRate);

	List<Loan> findByInterestRateGreaterThanEqual(BigDecimal interestRate);

	List<Loan> findByTenureMonths(Integer tenureMonths);
}
