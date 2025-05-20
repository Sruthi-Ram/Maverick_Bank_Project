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
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "Customers")
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private int customerId;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @Column(name = "name", nullable = false, length = 100)
    private String name;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "address", length = 255)
    private String address;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "aadhar_number", unique = true, length = 20)
    private String aadharNumber;

    @Column(name = "pan_number", unique = true, length = 15)
    private String panNumber;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    private LocalDateTime updatedAt = LocalDateTime.now();

    // Constructors
    public Customer() {
    }

    public Customer(User user, String name, String gender, String address, LocalDate dateOfBirth, String aadharNumber, String panNumber) {
        this.user = user;
        this.name = name;
        this.gender = gender;
        this.address = address;
        this.dateOfBirth = dateOfBirth;
        this.aadharNumber = aadharNumber;
        this.panNumber = panNumber;
    }

    // Getters
    public int getCustomerId() {
        return customerId;
    }

    public User getUser() {
        return user;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getAddress() {
        return address;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    // Setters
    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public void setAadharNumber(String aadharNumber) {
        this.aadharNumber = aadharNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", user=" + (user != null ? user.getUserId() : null) +
                ", name='" + name + '\'' +
                ", gender='" + gender + '\'' +
                ", address='" + address + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", aadharNumber='" + aadharNumber + '\'' +
                ", panNumber='" + panNumber + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Customer customer = (Customer) o;
        return customerId == customer.customerId && user.equals(customer.user) && aadharNumber.equals(customer.aadharNumber) && panNumber.equals(customer.panNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(customerId, user, aadharNumber, panNumber);
    }
}