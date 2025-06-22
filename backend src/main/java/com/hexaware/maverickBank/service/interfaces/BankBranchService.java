package com.hexaware.maverickbank.service.interfaces;

import java.util.List;

import com.hexaware.maverickbank.dto.BankBranchCreateRequestDTO;
import com.hexaware.maverickbank.dto.BankBranchDTO;
import com.hexaware.maverickbank.dto.BankBranchUpdateRequestDTO;

public interface BankBranchService {
    BankBranchDTO createBankBranch(BankBranchCreateRequestDTO bankBranchCreateRequestDTO);
    BankBranchDTO getBankBranchById(Long branchId);
    List<BankBranchDTO> getAllBankBranches();
    BankBranchDTO updateBankBranch(Long branchId, BankBranchUpdateRequestDTO bankBranchUpdateRequestDTO);
    void deleteBankBranch(Long branchId);
    BankBranchDTO getBankBranchByName(String name);
    BankBranchDTO getBankBranchByIfscPrefix(String ifscPrefix);
}