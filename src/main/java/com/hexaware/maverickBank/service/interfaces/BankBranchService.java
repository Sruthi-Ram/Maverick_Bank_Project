package com.hexaware.maverickBank.service.interfaces;

import com.hexaware.maverickBank.entity.BankBranch;
import java.util.List;

public interface BankBranchService {
    BankBranch createBankBranch(BankBranch branch);
    BankBranch getBankBranchById(Long branchId);
    List<BankBranch> getAllBankBranches();
    BankBranch updateBankBranch(Long branchId, BankBranch branch);
    void deleteBankBranch(Long branchId);
    BankBranch getBankBranchByName(String name);
    BankBranch getBankBranchByIfscPrefix(String ifscPrefix);
}