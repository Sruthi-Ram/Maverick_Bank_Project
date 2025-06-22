package com.hexaware.maverickbank.service.interfaces;

import java.util.List;

import com.hexaware.maverickbank.dto.LoanCreateRequestDTO;
import com.hexaware.maverickbank.dto.LoanDTO;
import com.hexaware.maverickbank.dto.LoanUpdateRequestDTO;

public interface LoanService {
    LoanDTO createLoan(LoanCreateRequestDTO loanCreateRequestDTO);
    LoanDTO getLoanById(Long loanId);
    List<LoanDTO> getAllLoans();
    LoanDTO updateLoan(Long loanId, LoanUpdateRequestDTO loanUpdateRequestDTO);
    void deleteLoan(Long loanId);
}