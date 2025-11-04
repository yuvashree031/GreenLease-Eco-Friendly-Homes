package com.greenlease.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Property model representing a rental property with eco-rating features
 */
public class Property {
    private Long id;
    private String title;
    private String description;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private BigDecimal rent;
    private String propertyType; // apartment, house, condo, etc.
    private Integer bedrooms;
    private Integer bathrooms;
    private Double squareFootage;
    
    // Eco-rating fields
    private Integer insulationRating; // 1-10 scale
    private Boolean solarPanels;
    private Integer solarRating; // 1-10 scale
    private Integer waterConservationRating; // 1-10 scale
    private Double greenSpaceProximity; // distance in miles
    private Integer energyEfficiencyRating; // 1-10 scale
    private Double overallEcoScore; // calculated overall score
    
    // Additional fields
    private Long landlordId;
    private String imageUrl;
    private Boolean isAvailable;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public Property() {}
    
    public Property(String title, String description, String address, String city, 
                   String state, String zipCode, BigDecimal rent) {
        this.title = title;
        this.description = description;
        this.address = address;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
        this.rent = rent;
        this.isAvailable = true;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getCity() { return city; }
    public void setCity(String city) { this.city = city; }
    
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    
    public String getZipCode() { return zipCode; }
    public void setZipCode(String zipCode) { this.zipCode = zipCode; }
    
    public BigDecimal getRent() { return rent; }
    public void setRent(BigDecimal rent) { this.rent = rent; }
    
    public String getPropertyType() { return propertyType; }
    public void setPropertyType(String propertyType) { this.propertyType = propertyType; }
    
    public Integer getBedrooms() { return bedrooms; }
    public void setBedrooms(Integer bedrooms) { this.bedrooms = bedrooms; }
    
    public Integer getBathrooms() { return bathrooms; }
    public void setBathrooms(Integer bathrooms) { this.bathrooms = bathrooms; }
    
    public Double getSquareFootage() { return squareFootage; }
    public void setSquareFootage(Double squareFootage) { this.squareFootage = squareFootage; }
    
    public Integer getInsulationRating() { return insulationRating; }
    public void setInsulationRating(Integer insulationRating) { this.insulationRating = insulationRating; }
    
    public Boolean getSolarPanels() { return solarPanels; }
    public void setSolarPanels(Boolean solarPanels) { this.solarPanels = solarPanels; }
    
    public Integer getSolarRating() { return solarRating; }
    public void setSolarRating(Integer solarRating) { this.solarRating = solarRating; }
    
    public Integer getWaterConservationRating() { return waterConservationRating; }
    public void setWaterConservationRating(Integer waterConservationRating) { 
        this.waterConservationRating = waterConservationRating; 
    }
    
    public Double getGreenSpaceProximity() { return greenSpaceProximity; }
    public void setGreenSpaceProximity(Double greenSpaceProximity) { 
        this.greenSpaceProximity = greenSpaceProximity; 
    }
    
    public Integer getEnergyEfficiencyRating() { return energyEfficiencyRating; }
    public void setEnergyEfficiencyRating(Integer energyEfficiencyRating) { 
        this.energyEfficiencyRating = energyEfficiencyRating; 
    }
    
    public Double getOverallEcoScore() { return overallEcoScore; }
    public void setOverallEcoScore(Double overallEcoScore) { this.overallEcoScore = overallEcoScore; }
    
    public Long getLandlordId() { return landlordId; }
    public void setLandlordId(Long landlordId) { this.landlordId = landlordId; }
    
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    
    public Boolean getIsAvailable() { return isAvailable; }
    public void setIsAvailable(Boolean isAvailable) { this.isAvailable = isAvailable; }
    
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    
    public LocalDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(LocalDateTime updatedAt) { this.updatedAt = updatedAt; }
    
    // Utility methods
    public String getFullAddress() {
        return String.format("%s, %s, %s %s", address, city, state, zipCode);
    }
    
    public String getEcoRatingDisplay() {
        if (overallEcoScore == null) return "Not Rated";
        if (overallEcoScore >= 8.0) return "Excellent";
        if (overallEcoScore >= 6.0) return "Good";
        if (overallEcoScore >= 4.0) return "Fair";
        return "Poor";
    }
    
    @Override
    public String toString() {
        return "Property{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", city='" + city + '\'' +
                ", rent=" + rent +
                ", overallEcoScore=" + overallEcoScore +
                '}';
    }
}