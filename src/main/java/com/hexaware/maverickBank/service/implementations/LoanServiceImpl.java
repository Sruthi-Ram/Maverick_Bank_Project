package com.hexaware.maverickBank.service.implementations;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexaware.maverickBank.entity.Loan;
import com.hexaware.maverickBank.repository.ILoanRepository;
import com.hexaware.maverickBank.service.interfaces.LoanService;

import jakarta.validation.ValidationException;

@Service
public class LoanServiceImpl implements LoanService {

    @Autowired
    private ILoanRepository loanRepository;

    private void validateLoan(Loan loan) {
        if (loan.getLoanType() == null || loan.getLoanType().isEmpty()) {
            throw new ValidationException("Loan type cannot be empty");
        }
        if (loan.getPrincipalAmount() == null || loan.getPrincipalAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new ValidationException("Principal amount must be positive");
        }
        if (loan.getInterestRate() == null || loan.getInterestRate().compareTo(BigDecimal.ZERO) < 0) {
            throw new ValidationException("Interest rate cannot be negative");
        }
        if (loan.getTenureMonths() == null || loan.getTenureMonths() <= 0) {
            throw new ValidationException("Tenure months must be positive");
        }
    }

    @Override
    public Loan createLoan(Loan loan) {
        validateLoan(loan);
        return loanRepository.save(loan);
    }

    @Override
    public Loan getLoanById(Long loanId) {
        return loanRepository.findById(loanId)
                .orElseThrow(() -> new NoSuchElementException("Loan not found with ID: " + loanId));
    }

    @Override
    public List<Loan> getAllLoans() {
        return loanRepository.findAll();
    }

    @Override
    public Loan updateLoan(Long loanId, Loan loan) {
        if (!loanRepository.existsById(loanId)) {
            throw new NoSuchElementException("Loan not found with ID: " + loanId);
        }
        loan.setLoanId(loanId);
        validateLoan(loan);
        return loanRepository.save(loan);
    }

    @Override
    public void deleteLoan(Long loanId) {
        if (!loanRepository.existsById(loanId)) {
            throw new NoSuchElementException("Loan not found with ID: " + loanId);
        }
        // Consider adding logic to prevent deleting loans with active applications or disbursements
        loanRepository.deleteById(loanId);
    }
}