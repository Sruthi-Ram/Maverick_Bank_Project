package com.hexaware.maverickBank.service.interfaces;

import com.hexaware.maverickBank.entity.LoanApplication;
import java.util.List;

public interface LoanApplicationService {
    LoanApplication createLoanApplication(LoanApplication loanApplication);
    LoanApplication getLoanApplicationById(Long applicationId);
    List<LoanApplication> getAllLoanApplications();
    LoanApplication updateLoanApplication(Long applicationId, LoanApplication loanApplication);
    void deleteLoanApplication(Long applicationId);
    List<LoanApplication> getLoanApplicationsByCustomerId(Long customerId);
}