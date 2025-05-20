package com.hexaware.maverickBank.entity;

import java.math.BigDecimal;
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
@Table(name = "Accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "account_id")
    private int accountId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @Column(name = "account_number", unique = true, nullable = false, length = 20)
    private String accountNumber;

    @Column(name = "account_type", nullable = false, length = 20)
    private String accountType; // "savings", "checking", "business"

    @Column(name = "ifsc_code", length = 15)
    private String ifscCode;

    @Column(name = "branch_name", length = 100)
    private String branchName;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "balance", precision = 15, scale = 2)
    private BigDecimal balance = BigDecimal.ZERO;

    @Column(name = "status", length = 10)
    private String status; // "active", "closed"

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Constructors
    public Account() {
    }

    public Account(Customer customer, String accountNumber, String accountType, String ifscCode, String branchName, String address) {
        this.customer = customer;
        this.accountNumber = accountNumber;
        this.accountType = accountType;
        this.ifscCode = ifscCode;
        this.branchName = branchName;
        this.address = address;
    }

    // Getters
    public int getAccountId() {
        return accountId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public String getAccountType() {
        return accountType;
    }

    public String getIfscCode() {
        return ifscCode;
    }

    public String getBranchName() {
        return branchName;
    }

    public String getAddress() {
        return address;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public void setIfscCode(String ifscCode) {
        this.ifscCode = ifscCode;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Account{" +
                "accountId=" + accountId +
                ", customer=" + (customer != null ? customer.getCustomerId() : null) +
                ", accountNumber='" + accountNumber + '\'' +
                ", accountType='" + accountType + '\'' +
                ", ifscCode='" + ifscCode + '\'' +
                ", branchName='" + branchName + '\'' +
                ", address='" + address + '\'' +
                ", balance=" + balance +
                ", status='" + status + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Account account = (Account) o;
        return accountId == account.accountId && customer.equals(account.customer) && accountNumber.equals(account.accountNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(accountId, customer, accountNumber);
    }
}