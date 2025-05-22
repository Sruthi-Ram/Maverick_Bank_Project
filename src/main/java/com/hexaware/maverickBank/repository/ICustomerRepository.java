package com.hexaware.maverickBank.repository;

import com.hexaware.maverickBank.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findByUser_UserId(int userId);
}