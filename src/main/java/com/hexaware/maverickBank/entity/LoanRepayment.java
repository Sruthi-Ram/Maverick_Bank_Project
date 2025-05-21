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
@Table(name = "LoanRepayments")
public class LoanRepayment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int repaymentId;

    @ManyToOne
    @JoinColumn(name = "applicationId")
    private LoanApplication loanApplication;

    @ManyToOne
    @JoinColumn(name = "accountId")
    private Account account; // Account from which payment is made

    private Integer installmentNumber;
    private LocalDate dueDate;
    private LocalDate paymentDate;
    private BigDecimal amountPaid;
    private BigDecimal principalPaid;
    private BigDecimal interestPaid;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public LoanRepayment() {
    }

    public int getRepaymentId() {
        return repaymentId;
    }

    public void setRepaymentId(int repaymentId) {
        this.repaymentId = repaymentId;
    }

    public LoanApplication getLoanApplication() {
        return loanApplication;
    }

    public void setLoanApplication(LoanApplication loanApplication) {
        this.loanApplication = loanApplication;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public Integer getInstallmentNumber() {
        return installmentNumber;
    }

    public void setInstallmentNumber(Integer installmentNumber) {
        this.installmentNumber = installmentNumber;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(LocalDate paymentDate) {
        this.paymentDate = paymentDate;
    }

    public BigDecimal getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(BigDecimal amountPaid) {
        this.amountPaid = amountPaid;
    }

    public BigDecimal getPrincipalPaid() {
        return principalPaid;
    }

    public void setPrincipalPaid(BigDecimal principalPaid) {
        this.principalPaid = principalPaid;
    }

    public BigDecimal getInterestPaid() {
        return interestPaid;
    }

    public void setInterestPaid(BigDecimal interestPaid) {
        this.interestPaid = interestPaid;
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
        return "LoanRepayment{" +
                "repaymentId=" + repaymentId +
                ", loanApplication=" + (loanApplication != null ? loanApplication.getApplicationId() : null) +
                ", account=" + (account != null ? account.getAccountId() : null) +
                ", installmentNumber=" + installmentNumber +
                ", dueDate=" + dueDate +
                ", paymentDate=" + paymentDate +
                ", amountPaid=" + amountPaid +
                ", principalPaid=" + principalPaid +
                ", interestPaid=" + interestPaid +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}