package com.hexaware.maverickBank.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.hexaware.maverickBank.entity.Customer;

@Repository
public interface ICustomerRepository extends JpaRepository<Customer, Integer> {
	Customer findByUser_UserId(int userId);

	Customer findByName(String name);

	Customer findByAadharNumber(String aadharNumber);

	Customer findByPanNumber(String panNumber);
}
