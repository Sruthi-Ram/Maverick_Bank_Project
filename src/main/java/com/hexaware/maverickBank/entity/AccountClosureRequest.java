package com.hexaware.maverickBank.entity;

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
@Table(name = "AccountClosureRequests")
public class AccountClosureRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "closure_request_id")
    private int closureRequestId;

    @ManyToOne
    @JoinColumn(name = "account_id", nullable = false)
    private Account account;

    @Column(name = "request_date", updatable = false)
    private LocalDateTime requestDate = LocalDateTime.now();

    @Column(name = "status", length = 20)
    private String status; // "pending", "approved", "rejected"

    @ManyToOne
    @JoinColumn(name = "approved_by_employee_id")
    private BankEmployee approvedByEmployee;

    @Column(name = "closure_date")
    private LocalDate closureDate;

    // Constructors
    public AccountClosureRequest() {
    }

    public AccountClosureRequest(Account account, String status) {
        this.account = account;
        this.status = status;
    }

    // Getters
    public int getClosureRequestId() {
        return closureRequestId;
    }

    public Account getAccount() {
        return account;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public String getStatus() {
        return status;
    }

    public BankEmployee getApprovedByEmployee() {
        return approvedByEmployee;
    }

    public LocalDate getClosureDate() {
        return closureDate;
    }

    // Setters
    public void setClosureRequestId(int closureRequestId) {
        this.closureRequestId = closureRequestId;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setApprovedByEmployee(BankEmployee approvedByEmployee) {
        this.approvedByEmployee = approvedByEmployee;
    }

    public void setClosureDate(LocalDate closureDate) {
        this.closureDate = closureDate;
    }

    @Override
    public String toString() {
        return "AccountClosureRequest{" +
                "closureRequestId=" + closureRequestId +
                ", account=" + (account != null ? account.getAccountId() : null) +
                ", requestDate=" + requestDate +
                ", status='" + status + '\'' +
                ", approvedByEmployee=" + (approvedByEmployee != null ? approvedByEmployee.getEmployeeId() : null) +
                ", closureDate=" + closureDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountClosureRequest that = (AccountClosureRequest) o;
        return closureRequestId == that.closureRequestId && account.equals(that.account);
    }

    @Override
    public int hashCode() {
        return Objects.hash(closureRequestId, account);
    }
}