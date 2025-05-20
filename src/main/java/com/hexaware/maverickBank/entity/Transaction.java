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
@Table(name = "Transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "transaction_id")
    private int transactionId;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "transaction_type", nullable = false, length = 20)
    private String transactionType; // "deposit", "withdrawal", "transfer"

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "transaction_date", updatable = false)
    private LocalDateTime transactionDate = LocalDateTime.now();

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "reference_account_number", length = 20)
    private String referenceAccountNumber; // For transfers

    @Column(name = "status", length = 10)
    private String status; // "pending", "completed", "failed"

    // Constructors
    public Transaction() {
    }

    public Transaction(Account account, String transactionType, BigDecimal amount, String description, String referenceAccountNumber, String status) {
        this.account = account;
        this.transactionType = transactionType;
        this.amount = amount;
        this.description = description;
        this.referenceAccountNumber = referenceAccountNumber;
        this.status = status;
    }

    // Getters
    public int getTransactionId() {
        return transactionId;
    }

    public Account getAccount() {
        return account;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public LocalDateTime getTransactionDate() {
        return transactionDate;
    }

    public String getDescription() {
        return description;
    }

    public String getReferenceAccountNumber() {
        return referenceAccountNumber;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public void setTransactionDate(LocalDateTime transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReferenceAccountNumber(String referenceAccountNumber) {
        this.referenceAccountNumber = referenceAccountNumber;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionId=" + transactionId +
                ", account=" + (account != null ? account.getAccountId() : null) +
                ", transactionType='" + transactionType + '\'' +
                ", amount=" + amount +
                ", transactionDate=" + transactionDate +
                ", description='" + description + '\'' +
                ", referenceAccountNumber='" + referenceAccountNumber + '\'' +
                ", status='" + status + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Transaction that = (Transaction) o;
        return transactionId == that.transactionId && account.equals(that.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(transactionId, account);
    }
}