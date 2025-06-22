package com.hexaware.maverickbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hexaware.maverickbank.dto.entity.BankBranch;
@Repository
public interface IBankBranchRepository extends JpaRepository<BankBranch, Long> {
    BankBranch findByName(String name);
    BankBranch findByIfscPrefix(String ifscPrefix);
}