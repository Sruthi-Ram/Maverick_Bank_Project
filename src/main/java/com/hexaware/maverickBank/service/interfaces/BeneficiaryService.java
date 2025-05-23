package com.hexaware.maverickBank.service.interfaces;

import java.util.List;

import com.hexaware.maverickBank.dto.BeneficiaryCreateRequestDTO;
import com.hexaware.maverickBank.dto.BeneficiaryDTO;
import com.hexaware.maverickBank.dto.BeneficiaryUpdateRequestDTO;

public interface BeneficiaryService {
    BeneficiaryDTO createBeneficiary(BeneficiaryCreateRequestDTO beneficiaryCreateRequestDTO);
    BeneficiaryDTO getBeneficiaryById(Long beneficiaryId);
    List<BeneficiaryDTO> getAllBeneficiaries();
    BeneficiaryDTO updateBeneficiary(Long beneficiaryId, BeneficiaryUpdateRequestDTO beneficiaryUpdateRequestDTO);
    void deleteBeneficiary(Long beneficiaryId);
    List<BeneficiaryDTO> getBeneficiariesByCustomerId(Long customerId);
}