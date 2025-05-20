package com.hexaware.maverickBank.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.hexaware.maverickBank.entity.Beneficiary;

@Repository
public interface IBeneficiaryRepository {
	List<Beneficiary> findByCustomer_CustomerId(int customerId);
}
