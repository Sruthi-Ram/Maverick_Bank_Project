package com.hexaware.maverickBank.service.interfaces;

import com.hexaware.maverickBank.dto.LoanCreateRequestDTO;
import com.hexaware.maverickBank.dto.LoanDTO;
import com.hexaware.maverickBank.dto.LoanUpdateRequestDTO;
import java.util.List;

public interface LoanService {
    LoanDTO createLoan(LoanCreateRequestDTO loanCreateRequestDTO);
    LoanDTO getLoanById(Long loanId);
    List<LoanDTO> getAllLoans();
    LoanDTO updateLoan(Long loanId, LoanUpdateRequestDTO loanUpdateRequestDTO);
    void deleteLoan(Long loanId);
}