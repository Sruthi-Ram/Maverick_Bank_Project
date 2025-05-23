package com.hexaware.maverickBank.service.implementations;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hexaware.maverickBank.entity.BankBranch;
import com.hexaware.maverickBank.repository.IBankBranchRepository;
import com.hexaware.maverickBank.service.interfaces.BankBranchService;

import jakarta.validation.ValidationException;

@Service
public class BankBranchServiceImpl implements BankBranchService {

    @Autowired
    private IBankBranchRepository bankBranchRepository;

    private boolean isValidIfscPrefix(String ifscPrefix) {
        Pattern pattern = Pattern.compile("^[A-Za-z]{4}$");
        Matcher matcher = pattern.matcher(ifscPrefix);
        return matcher.matches();
    }

    @Override
    public BankBranch createBankBranch(BankBranch branch) {
        if (branch.getName() == null || branch.getName().isEmpty()) {
            throw new ValidationException("Branch name cannot be empty");
        }
        if (branch.getAddress() == null || branch.getAddress().isEmpty()) {
            throw new ValidationException("Branch address cannot be empty");
        }
        if (branch.getIfscPrefix() == null || branch.getIfscPrefix().isEmpty() || !isValidIfscPrefix(branch.getIfscPrefix())) {
            throw new ValidationException("Invalid IFSC prefix (must be 4 alphabetic characters)");
        }
        if (bankBranchRepository.findByName(branch.getName()) != null) {
            throw new ValidationException("Branch with name " + branch.getName() + " already exists");
        }
        if (bankBranchRepository.findByIfscPrefix(branch.getIfscPrefix()) != null) {
            throw new ValidationException("Branch with IFSC prefix " + branch.getIfscPrefix() + " already exists");
        }
        return bankBranchRepository.save(branch);
    }

    @Override
    public BankBranch getBankBranchById(Long branchId) {
        return bankBranchRepository.findById(branchId)
                .orElseThrow(() -> new NoSuchElementException("Bank branch not found with ID: " + branchId));
    }

    @Override
    public List<BankBranch> getAllBankBranches() {
        return bankBranchRepository.findAll();
    }

    @Override
    public BankBranch updateBankBranch(Long branchId, BankBranch branch) {
        BankBranch existingBranch = bankBranchRepository.findById(branchId)
                .orElseThrow(() -> new NoSuchElementException("Bank branch not found with ID: " + branchId));
        if (branch.getName() != null && !branch.getName().isEmpty() && !existingBranch.getName().equals(branch.getName()) && bankBranchRepository.findByName(branch.getName()) != null) {
            throw new ValidationException("Branch with name " + branch.getName() + " already exists");
        }
        if (branch.getIfscPrefix() != null && !branch.getIfscPrefix().isEmpty() && !existingBranch.getIfscPrefix().equals(branch.getIfscPrefix()) && bankBranchRepository.findByIfscPrefix(branch.getIfscPrefix()) != null) {
            throw new ValidationException("Branch with IFSC prefix " + branch.getIfscPrefix() + " already exists");
        }
        branch.setBranchId(branchId);
        if (branch.getName() != null) existingBranch.setName(branch.getName());
        if (branch.getAddress() != null) existingBranch.setAddress(branch.getAddress());
        if (branch.getIfscPrefix() != null) existingBranch.setIfscPrefix(branch.getIfscPrefix());
        return bankBranchRepository.save(existingBranch);
    }

    @Override
    public void deleteBankBranch(Long branchId) {
        if (!bankBranchRepository.existsById(branchId)) {
            throw new NoSuchElementException("Bank branch not found with ID: " + branchId);
        }
        // Consider adding logic to prevent deleting branches with associated accounts or employees
        bankBranchRepository.deleteById(branchId);
    }

    @Override
    public BankBranch getBankBranchByName(String name) {
        BankBranch branch = bankBranchRepository.findByName(name);
        if (branch == null) {
            throw new NoSuchElementException("Bank branch not found with name: " + name);
        }
        return branch;
    }

    @Override
    public BankBranch getBankBranchByIfscPrefix(String ifscPrefix) {
        BankBranch branch = bankBranchRepository.findByIfscPrefix(ifscPrefix);
        if (branch == null) {
            throw new NoSuchElementException("Bank branch not found with IFSC prefix: " + ifscPrefix);
        }
        return branch;
    }
}