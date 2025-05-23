package com.hexaware.maverickBank.service.interfaces;

import com.hexaware.maverickBank.entity.Beneficiary;
import java.util.List;

public interface BeneficiaryService {
    Beneficiary createBeneficiary(Beneficiary beneficiary);
    Beneficiary getBeneficiaryById(Long beneficiaryId);
    List<Beneficiary> getAllBeneficiaries();
    Beneficiary updateBeneficiary(Long beneficiaryId, Beneficiary beneficiary);
    void deleteBeneficiary(Long beneficiaryId);
    List<Beneficiary> getBeneficiariesByCustomerId(Long customerId);
}