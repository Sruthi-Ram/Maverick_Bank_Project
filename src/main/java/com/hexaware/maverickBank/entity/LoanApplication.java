package com.hexaware.maverickBank.entity;

import java.math.BigDecimal;
import java.time.LocalDate;
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
@Table(name = "LoanApplications")
public class LoanApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "loan_application_id")
    private int loanApplicationId;

    @ManyToOne
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne
    @JoinColumn(name = "loan_id", nullable = false)
    private Loan loan;

    @Column(name = "application_date", updatable = false)
    private LocalDateTime applicationDate = LocalDateTime.now();

    @Column(name = "loan_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal loanAmount;

    @Column(name = "purpose", length = 255)
    private String purpose;

    @Column(name = "status", length = 20)
    private String status; // "pending", "approved", "rejected", "disbursed"

    @ManyToOne
    @JoinColumn(name = "approved_by_employee_id")
    private BankEmployee approvedByEmployee;

    @Column(name = "disbursement_date")
    private LocalDate disbursementDate;

    // Constructors
    public LoanApplication() {
    }

    public LoanApplication(Customer customer, Loan loan, BigDecimal loanAmount, String purpose, String status) {
        this.customer = customer;
        this.loan = loan;
        this.loanAmount = loanAmount;
        this.purpose = purpose;
        this.status = status;
    }

    // Getters
    public int getLoanApplicationId() {
        return loanApplicationId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public Loan getLoan() {
        return loan;
    }

    public LocalDateTime getApplicationDate() {
        return applicationDate;
    }

    public BigDecimal getLoanAmount() {
        return loanAmount;
    }

    public String getPurpose() {
        return purpose;
    }

    public String getStatus() {
        return status;
    }

    public BankEmployee getApprovedByEmployee() {
        return approvedByEmployee;
    }

    public LocalDate getDisbursementDate() {
        return disbursementDate;
    }

    // Setters
    public void setLoanApplicationId(int loanApplicationId) {
        this.loanApplicationId = loanApplicationId;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public void setApplicationDate(LocalDateTime applicationDate) {
        this.applicationDate = applicationDate;
    }

    public void setLoanAmount(BigDecimal loanAmount) {
        this.loanAmount = loanAmount;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setApprovedByEmployee(BankEmployee approvedByEmployee) {
        this.approvedByEmployee = approvedByEmployee;
    }

    public void setDisbursementDate(LocalDate disbursementDate) {
        this.disbursementDate = disbursementDate;
    }

    @Override
    public String toString() {
        return "LoanApplication{" +
                "loanApplicationId=" + loanApplicationId +
                ", customer=" + (customer != null ? customer.getCustomerId() : null) +
                ", loan=" + (loan != null ? loan.getLoanId() : null) +
                ", applicationDate=" + applicationDate +
                ", loanAmount=" + loanAmount +
                ", purpose='" + purpose + '\'' +
                ", status='" + status + '\'' +
                ", approvedByEmployee=" + (approvedByEmployee != null ? approvedByEmployee.getEmployeeId() : null) +
                ", disbursementDate=" + disbursementDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoanApplication that = (LoanApplication) o;
        return loanApplicationId == that.loanApplicationId && customer.equals(that.customer) && loan.equals(that.loan);
    }

    @Override
    public int hashCode() {
        return Objects.hash(loanApplicationId, customer, loan);
    }
}