package com.hexaware.maverickbank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hexaware.maverickbank.dto.entity.Customer;

@Repository
public interface ICustomerRepository extends JpaRepository<Customer, Long> {
    Customer findByUser_UserId(Long userId);    
}