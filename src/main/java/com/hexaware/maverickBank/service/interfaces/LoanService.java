package com.hexaware.maverickBank.service.interfaces;

import com.hexaware.maverickBank.entity.Loan;
import java.util.List;

public interface LoanService {
    Loan createLoan(Loan loan);
    Loan getLoanById(Long loanId);
    List<Loan> getAllLoans();
    Loan updateLoan(Long loanId, Loan loan);
    void deleteLoan(Long loanId);
}