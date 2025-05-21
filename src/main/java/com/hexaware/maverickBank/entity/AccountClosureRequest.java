package com.hexaware.maverickBank.entity;

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
@Table(name = "AccountClosureRequests")
public class AccountClosureRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int requestId;

    @ManyToOne
    @JoinColumn(name = "accountId")
    private Account account;

    private String reason;
    private String status; // e.g., "pending", "approved", "rejected"
    private LocalDateTime requestDate;

    @ManyToOne
    @JoinColumn(name = "approvedBy")
    private BankEmployee approvedBy;

    private LocalDate closureDate;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public AccountClosureRequest() {
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public BankEmployee getApprovedBy() {
        return approvedBy;
    }

    public void setApprovedBy(BankEmployee approvedBy) {
        this.approvedBy = approvedBy;
    }

    public LocalDate getClosureDate() {
        return closureDate;
    }

    public void setClosureDate(LocalDate closureDate) {
        this.closureDate = closureDate;
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
        return "AccountClosureRequest{" +
                "requestId=" + requestId +
                ", account=" + (account != null ? account.getAccountId() : null) +
                ", reason='" + reason + '\'' +
                ", status='" + status + '\'' +
                ", requestDate=" + requestDate +
                ", approvedBy=" + (approvedBy != null ? approvedBy.getEmployeeId() : null) +
                ", closureDate=" + closureDate +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}