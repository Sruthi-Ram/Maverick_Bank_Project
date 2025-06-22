package com.hexaware.maverickbank.service.interfaces;

import java.util.List;

import com.hexaware.maverickbank.dto.AccountClosureRequestCreateRequestDTO;
import com.hexaware.maverickbank.dto.AccountClosureRequestDTO;

public interface AccountClosureRequestService {
    AccountClosureRequestDTO createAccountClosureRequest(AccountClosureRequestCreateRequestDTO requestDTO);
    AccountClosureRequestDTO getAccountClosureRequestById(Long closureRequestId);
    List<AccountClosureRequestDTO> getAllAccountClosureRequests();
    AccountClosureRequestDTO updateAccountClosureRequest(Long closureRequestId, AccountClosureRequestDTO requestDTO);
    void deleteAccountClosureRequest(Long closureRequestId);
    List<AccountClosureRequestDTO> getAccountClosureRequestsByCustomerId(Long customerId);
    AccountClosureRequestDTO getAccountClosureRequestByAccountId(Long accountId);
}