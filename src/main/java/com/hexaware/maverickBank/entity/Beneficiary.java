package com.hexaware.maverickBank.entity;

import java.time.LocalDateTime;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Beneficiaries")
public class Beneficiary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "beneficiary_id")
    private int beneficiaryId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "beneficiary_account_number", nullable = false, length = 20)
    private String beneficiaryAccountNumber;

    @Column(name = "beneficiary_name", nullable = false, length = 100)
    private String beneficiaryName;

    @Column(name = "bank_name", length = 100)
    private String bankName;

    @Column(name = "branch_name", length = 100)
    private String branchName;

    @Column(name = "ifsc_code", length = 15)
    private String ifscCode;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // Constructors
    public Beneficiary() {
    }

    public Beneficiary(Customer customer, String beneficiaryAccountNumber, String beneficiaryName, String bankName, String branchName, String ifscCode) {
        this.customer = customer;
        this.beneficiaryAccountNumber = beneficiaryAccountNumber;
        this.beneficiaryName = beneficiaryName;
        this.bankName = bankName;
        this.branchName = branchName;
        this.ifscCode = ifscCode;
    }

    // Getters
    public int getBeneficiaryId() {
        return beneficiaryId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getBeneficiaryAccountNumber() {
        return beneficiaryAccountNumber;
    }

    public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public String getBankName() {
        return bankName;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // Setters
    public void setBeneficiaryId(int beneficiaryId) {
        this.beneficiaryId = beneficiaryId;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setBeneficiaryAccountNumber(String beneficiaryAccountNumber) {
        this.beneficiaryAccountNumber = beneficiaryAccountNumber;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Beneficiary{" +
                "beneficiaryId=" + beneficiaryId +
                ", customer=" + (customer != null ? customer.getCustomerId() : null) +
                ", beneficiaryAccountNumber='" + beneficiaryAccountNumber + '\'' +
                ", beneficiaryName='" + beneficiaryName + '\'' +
                ", bankName='" + bankName + '\'' +
                ", branchName='" + branchName + '\'' +
                ", ifscCode='" + ifscCode + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Beneficiary that = (Beneficiary) o;
        return beneficiaryId == that.beneficiaryId && customer.equals(that.customer) && beneficiaryAccountNumber.equals(that.beneficiaryAccountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(beneficiaryId, customer, beneficiaryAccountNumber);
    }
}