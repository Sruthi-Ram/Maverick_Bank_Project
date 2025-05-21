package com.hexaware.maverickBank.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "LoanApplications")
public class LoanApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int applicationId;

    @ManyToOne
    @JoinColumn(name = "customerId")
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "loanId")
    private Loan loan;

    private BigDecimal amountApplied;
    private String purpose;
    private String status; // e.g., "pending", "approved", "rejected", "disbursed"
    private LocalDateTime applicationDate;

    @ManyToOne
    @JoinColumn(name = "approvedBy")
    private BankEmployee approvedBy;

    private String rejectionReason;

    private LocalDate disbursementDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public LoanApplication() {
    }

    public int getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(int applicationId) {
        this.applicationId = applicationId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public BigDecimal getAmountApplied() {
        return amountApplied;
    }

    public void setAmountApplied(BigDecimal amountApplied) {
        this.amountApplied = amountApplied;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    public BankEmployee getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(BankEmployee approvedBy) {
        this.approvedBy = approvedBy;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }

    public void setRejectionReason(String rejectionReason) {
        this.rejectionReason = rejectionReason;
    }

    public LocalDate getDisbursementDate() {
        return disbursementDate;
    }

    public void setDisbursementDate(LocalDate disbursementDate) {
        this.disbursementDate = disbursementDate;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "LoanApplication{" +
                "applicationId=" + applicationId +
                ", customer=" + (customer != null ? customer.getCustomerId() : null) +
                ", loan=" + (loan != null ? loan.getLoanId() : null) +
                ", amountApplied=" + amountApplied +
                ", purpose='" + purpose + '\'' +
                ", status='" + status + '\'' +
                ", applicationDate=" + applicationDate +
                ", approvedBy=" + (approvedBy != null ? approvedBy.getEmployeeId() : null) +
                ", rejectionReason='" + rejectionReason + '\'' +
                ", disbursementDate=" + disbursementDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}