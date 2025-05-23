package com.hexaware.maverickBank.service.interfaces;

import com.hexaware.maverickBank.entity.AccountClosureRequest;
import java.util.List;

public interface AccountClosureRequestService {
    AccountClosureRequest createAccountClosureRequest(AccountClosureRequest request);
    AccountClosureRequest getAccountClosureRequestById(Long closureRequestId);
    List<AccountClosureRequest> getAllAccountClosureRequests();
    AccountClosureRequest updateAccountClosureRequest(Long closureRequestId, AccountClosureRequest request);
    void deleteAccountClosureRequest(Long closureRequestId);
    List<AccountClosureRequest> getAccountClosureRequestsByCustomerId(Long customerId);
    AccountClosureRequest getAccountClosureRequestByAccountId(Long accountId);
}