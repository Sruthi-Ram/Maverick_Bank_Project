package com.hexaware.maverickBank.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "BankBranches")
public class BankBranch {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "branch_id")
    private int branchId;

    @Column(name = "bank_name", nullable = false, length = 100)
    private String bankName;

    @Column(name = "branch_name", nullable = false, length = 100)
    private String branchName;

    @Column(name = "ifsc_code", unique = true, nullable = false, length = 15)
    private String ifscCode;

    @Column(name = "address", length = 255)
    private String address;

    // Constructors
    public BankBranch() {
    }

    public BankBranch(String bankName, String branchName, String ifscCode, String address) {
        this.bankName = bankName;
        this.branchName = branchName;
        this.ifscCode = ifscCode;
        this.address = address;
    }

    // Getters
    public int getBranchId() {
        return branchId;
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

    public String getAddress() {
        return address;
    }

    // Setters
    public void setBranchId(int branchId) {
        this.branchId = branchId;
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

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "BankBranch{" +
                "branchId=" + branchId +
                ", bankName='" + bankName + '\'' +
                ", branchName='" + branchName + '\'' +
                ", ifscCode='" + ifscCode + '\'' +
                ", address='" + address + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BankBranch that = (BankBranch) o;
        return branchId == that.branchId && bankName.equals(that.bankName) && branchName.equals(that.branchName) && ifscCode.equals(that.ifscCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(branchId, bankName, branchName, ifscCode);
    }
}