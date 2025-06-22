package com.hexaware.maverickbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hexaware.maverickbank.dto.entity.BankEmployee;

@Repository
public interface IBankEmployeeRepository extends JpaRepository<BankEmployee, Long> {
    BankEmployee findByUser_UserId(Long userId);
    //BankEmployee findByUsername(String username);
}