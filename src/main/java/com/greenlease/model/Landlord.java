package com.greenlease.model;

import java.time.LocalDateTime;

/**
 * Landlord model representing property owners
 */
public class Landlord {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String company;
    private Boolean isVerified;
    private Double sustainabilityScore; // Average eco-score of their properties
    private Integer totalProperties;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public Landlord() {}
    
    public Landlord(String firstName, String lastName, String email, String phone) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phone = phone;
        this.isVerified = false;
        this.totalProperties = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }
    
    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    
    public String getCompany() { return company; }
    public void setCompany(String company) { this.company = company; }
    
    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }
    
    public Double getSustainabilityScore() { return sustainabilityScore; }
    public void setSustainabilityScore(Double sustainabilityScore) { 
        this.sustainabilityScore = sustainabilityScore; 
    }
    
    public Integer getTotalProperties() { return totalProperties; }
    public void setTotalProperties(Integer totalProperties) { this.totalProperties = totalProperties; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Utility methods
    public String getFullName() {
        return firstName + " " + lastName;
    }
    
    public String getSustainabilityLevel() {
        if (sustainabilityScore == null) return "Not Rated";
        if (sustainabilityScore >= 8.0) return "Eco Champion";
        if (sustainabilityScore >= 6.0) return "Green Leader";
        if (sustainabilityScore >= 4.0) return "Eco Friendly";
        return "Standard";
    }
    
    @Override
    public String toString() {
        return "Landlord{" +
                "id=" + id +
                ", fullName='" + getFullName() + '\'' +
                ", email='" + email + '\'' +
                ", sustainabilityScore=" + sustainabilityScore +
                '}';
    }
}