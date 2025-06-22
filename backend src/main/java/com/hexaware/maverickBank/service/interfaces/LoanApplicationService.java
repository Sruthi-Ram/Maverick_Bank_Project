package com.hexaware.maverickbank.service.interfaces;

import java.util.List;

import com.hexaware.maverickbank.dto.LoanApplicationCreateRequestDTO;
import com.hexaware.maverickbank.dto.LoanApplicationDTO;
import com.hexaware.maverickbank.dto.LoanApplicationUpdateRequestDTO;

public interface LoanApplicationService {
    LoanApplicationDTO createLoanApplication(LoanApplicationCreateRequestDTO loanApplicationCreateRequestDTO);
    LoanApplicationDTO getLoanApplicationById(Long applicationId);
    List<LoanApplicationDTO> getAllLoanApplications();
    LoanApplicationDTO updateLoanApplication(Long applicationId, LoanApplicationUpdateRequestDTO loanApplicationUpdateRequestDTO);
    void deleteLoanApplication(Long applicationId);
    List<LoanApplicationDTO> getLoanApplicationsByCustomerId(Long customerId);
}