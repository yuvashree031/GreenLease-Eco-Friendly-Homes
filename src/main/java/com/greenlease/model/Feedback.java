package com.greenlease.model;

import java.time.LocalDateTime;

/**
 * Feedback model for tenant reviews and ratings
 */
public class Feedback {
    private Long id;
    private Long propertyId;
    private String tenantName;
    private String tenantEmail;
    private Integer overallRating; // 1-5 stars
    private Integer ecoRating; // 1-5 stars for eco features
    private String comment;
    private Boolean isVerified;
    private Boolean isRecommended;
    private LocalDateTime createdAt;
    
    // Specific eco-feature ratings
    private Integer insulationExperience; // 1-5 stars
    private Integer energyBillSatisfaction; // 1-5 stars
    private Integer solarSystemSatisfaction; // 1-5 stars
    private Integer waterEfficiencySatisfaction; // 1-5 stars
    private Integer greenSpaceSatisfaction; // 1-5 stars
    
    // Constructors
    public Feedback() {}
    
    public Feedback(Long propertyId, String tenantName, String tenantEmail, 
                   Integer overallRating, String comment) {
        this.propertyId = propertyId;
        this.tenantName = tenantName;
        this.tenantEmail = tenantEmail;
        this.overallRating = overallRating;
        this.comment = comment;
        this.isVerified = false;
        this.createdAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getPropertyId() { return propertyId; }
    public void setPropertyId(Long propertyId) { this.propertyId = propertyId; }
    
    public String getTenantName() { return tenantName; }
    public void setTenantName(String tenantName) { this.tenantName = tenantName; }
    
    public String getTenantEmail() { return tenantEmail; }
    public void setTenantEmail(String tenantEmail) { this.tenantEmail = tenantEmail; }
    
    public Integer getOverallRating() { return overallRating; }
    public void setOverallRating(Integer overallRating) { this.overallRating = overallRating; }
    
    public Integer getEcoRating() { return ecoRating; }
    public void setEcoRating(Integer ecoRating) { this.ecoRating = ecoRating; }
    
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    
    public Boolean getIsVerified() { return isVerified; }
    public void setIsVerified(Boolean isVerified) { this.isVerified = isVerified; }
    
    public Boolean getIsRecommended() { return isRecommended; }
    public void setIsRecommended(Boolean isRecommended) { this.isRecommended = isRecommended; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public Integer getInsulationExperience() { return insulationExperience; }
    public void setInsulationExperience(Integer insulationExperience) { 
        this.insulationExperience = insulationExperience; 
    }
    
    public Integer getEnergyBillSatisfaction() { return energyBillSatisfaction; }
    public void setEnergyBillSatisfaction(Integer energyBillSatisfaction) { 
        this.energyBillSatisfaction = energyBillSatisfaction; 
    }
    
    public Integer getSolarSystemSatisfaction() { return solarSystemSatisfaction; }
    public void setSolarSystemSatisfaction(Integer solarSystemSatisfaction) { 
        this.solarSystemSatisfaction = solarSystemSatisfaction; 
    }
    
    public Integer getWaterEfficiencySatisfaction() { return waterEfficiencySatisfaction; }
    public void setWaterEfficiencySatisfaction(Integer waterEfficiencySatisfaction) { 
        this.waterEfficiencySatisfaction = waterEfficiencySatisfaction; 
    }
    
    public Integer getGreenSpaceSatisfaction() { return greenSpaceSatisfaction; }
    public void setGreenSpaceSatisfaction(Integer greenSpaceSatisfaction) { 
        this.greenSpaceSatisfaction = greenSpaceSatisfaction; 
    }
    
    // Utility methods
    public String getRatingStars() {
        if (overallRating == null) return "☆☆☆☆☆";
        return "★".repeat(overallRating) + "☆".repeat(5 - overallRating);
    }
    
    public String getEcoRatingStars() {
        if (ecoRating == null) return "☆☆☆☆☆";
        return "🌱".repeat(ecoRating) + "☆".repeat(5 - ecoRating);
    }
    
    @Override
    public String toString() {
        return "Feedback{" +
                "id=" + id +
                ", propertyId=" + propertyId +
                ", tenantName='" + tenantName + '\'' +
                ", overallRating=" + overallRating +
                ", ecoRating=" + ecoRating +
                '}';
    }
}