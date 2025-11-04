package com.greenlease.service;

import com.greenlease.model.Property;
import com.greenlease.repository.PropertyRepository;
import com.greenlease.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

/**
 * Service layer for Property operations
 */
@Service
public class PropertyService {
    
    @Autowired
    private PropertyRepository propertyRepository;
    
    @SuppressWarnings("unused")
    @Autowired
    private FeedbackRepository feedbackRepository;
    
    public List<Property> getAllProperties() {
        return propertyRepository.findAll();
    }
    
    public List<Property> getAvailableProperties() {
        return propertyRepository.findAvailable();
    }
    
    public Optional<Property> getPropertyById(Long id) {
        return propertyRepository.findById(id);
    }
    
    public List<Property> searchByCity(String city) {
        return propertyRepository.findByCity(city);
    }
    
    public List<Property> filterByEcoScore(double minScore, double maxScore) {
        return propertyRepository.findByEcoScoreRange(minScore, maxScore);
    }
    
    public List<Property> filterByRent(BigDecimal minRent, BigDecimal maxRent) {
        return propertyRepository.findByRentRange(minRent, maxRent);
    }
    
    public List<Property> filterBySolarPanels(boolean hasSolar) {
        return propertyRepository.findBySolarPanels(hasSolar);
    }
    
    public Property saveProperty(Property property) {
        // Calculate overall eco score before saving
        calculateOverallEcoScore(property);
        return propertyRepository.save(property);
    }
    
    public void deleteProperty(Long id) {
        propertyRepository.deleteById(id);
    }
    
    /**
     * Calculate overall eco score based on various eco-rating factors
     */
    private void calculateOverallEcoScore(Property property) {
        double totalScore = 0.0;
        int factorCount = 0;
        
        // Insulation rating (weight: 20%)
        if (property.getInsulationRating() != null && property.getInsulationRating() > 0) {
            totalScore += property.getInsulationRating() * 0.2;
            factorCount++;
        }
        
        // Solar rating (weight: 25%)
        if (property.getSolarRating() != null && property.getSolarRating() > 0) {
            totalScore += property.getSolarRating() * 0.25;
            factorCount++;
        }
        
        // Water conservation rating (weight: 20%)
        if (property.getWaterConservationRating() != null && property.getWaterConservationRating() > 0) {
            totalScore += property.getWaterConservationRating() * 0.2;
            factorCount++;
        }
        
        // Energy efficiency rating (weight: 25%)
        if (property.getEnergyEfficiencyRating() != null && property.getEnergyEfficiencyRating() > 0) {
            totalScore += property.getEnergyEfficiencyRating() * 0.25;
            factorCount++;
        }
        
        // Green space proximity (weight: 10%) - inverse scoring (closer is better)
        if (property.getGreenSpaceProximity() != null) {
            double proximityScore = Math.max(0, 10 - property.getGreenSpaceProximity());
            totalScore += proximityScore * 0.1;
            factorCount++;
        }
        
        // Calculate average score
        if (factorCount > 0) {
            property.setOverallEcoScore(Math.round(totalScore * 100.0) / 100.0);
        } else {
            property.setOverallEcoScore(0.0);
        }
    }
    
    /**
     * Get properties with excellent eco ratings (8.0+)
     */
    public List<Property> getEcoExcellentProperties() {
        return propertyRepository.findByEcoScoreRange(8.0, 10.0);
    }
    
    /**
     * Get eco-friendly statistics
     */
    public EcoStatistics getEcoStatistics() {
        long totalProperties = propertyRepository.count();
        double averageEcoScore = propertyRepository.getAverageEcoScore();
        long solarProperties = propertyRepository.findBySolarPanels(true).size();
        long excellentProperties = getEcoExcellentProperties().size();
        
        return new EcoStatistics(totalProperties, averageEcoScore, solarProperties, excellentProperties);
    }
    
    /**
     * Inner class for eco statistics
     */
    public static class EcoStatistics {
        private final long totalProperties;
        private final double averageEcoScore;
        private final long solarProperties;
        private final long excellentProperties;
        
        public EcoStatistics(long totalProperties, double averageEcoScore, 
                           long solarProperties, long excellentProperties) {
            this.totalProperties = totalProperties;
            this.averageEcoScore = averageEcoScore;
            this.solarProperties = solarProperties;
            this.excellentProperties = excellentProperties;
        }
        
        public long getTotalProperties() { return totalProperties; }
        public double getAverageEcoScore() { return averageEcoScore; }
        public long getSolarProperties() { return solarProperties; }
        public long getExcellentProperties() { return excellentProperties; }
        
        public double getSolarPercentage() {
            return totalProperties > 0 ? (solarProperties * 100.0 / totalProperties) : 0.0;
        }
        
        public double getExcellentPercentage() {
            return totalProperties > 0 ? (excellentProperties * 100.0 / totalProperties) : 0.0;
        }
    }
}