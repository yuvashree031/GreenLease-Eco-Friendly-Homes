package com.greenlease.service;

import com.greenlease.model.Feedback;
import com.greenlease.repository.FeedbackRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service layer for Feedback operations
 */
@Service
public class FeedbackService {
    
    @Autowired
    private FeedbackRepository feedbackRepository;
    
    public List<Feedback> getAllFeedback() {
        return feedbackRepository.findAll();
    }
    
    public List<Feedback> getFeedbackByPropertyId(Long propertyId) {
        return feedbackRepository.findByPropertyId(propertyId);
    }
    
    public List<Feedback> getVerifiedFeedback() {
        return feedbackRepository.findVerified();
    }
    
    public Optional<Feedback> getFeedbackById(Long id) {
        return feedbackRepository.findById(id);
    }
    
    public Feedback saveFeedback(Feedback feedback) {
        // Auto-verify feedback if all required fields are present
        if (feedback.getTenantName() != null && 
            feedback.getTenantEmail() != null && 
            feedback.getOverallRating() != null && 
            feedback.getComment() != null && !feedback.getComment().trim().isEmpty()) {
            feedback.setIsVerified(true);
        }
        
        // Set recommendation based on ratings
        if (feedback.getOverallRating() != null && feedback.getOverallRating() >= 4) {
            feedback.setIsRecommended(true);
        }
        
        return feedbackRepository.save(feedback);
    }
    
    public void deleteFeedback(Long id) {
        feedbackRepository.deleteById(id);
    }
    
    public double getAverageRatingForProperty(Long propertyId) {
        return feedbackRepository.getAverageRatingForProperty(propertyId);
    }
    
    public double getAverageEcoRatingForProperty(Long propertyId) {
        return feedbackRepository.getAverageEcoRatingForProperty(propertyId);
    }
    
    public long getFeedbackCountForProperty(Long propertyId) {
        return feedbackRepository.countByPropertyId(propertyId);
    }
    
    /**
     * Verify feedback manually (admin function)
     */
    public Feedback verifyFeedback(Long feedbackId) {
        Optional<Feedback> feedbackOpt = feedbackRepository.findById(feedbackId);
        if (feedbackOpt.isPresent()) {
            Feedback feedback = feedbackOpt.get();
            feedback.setIsVerified(true);
            return feedbackRepository.save(feedback);
        }
        return null;
    }
    
    /**
     * Get feedback statistics for a property
     */
    public FeedbackStatistics getFeedbackStatistics(Long propertyId) {
        List<Feedback> feedbacks = getFeedbackByPropertyId(propertyId);
        long totalCount = feedbacks.size();
        long verifiedCount = feedbacks.stream().mapToLong(f -> f.getIsVerified() ? 1 : 0).sum();
        long recommendedCount = feedbacks.stream().mapToLong(f -> f.getIsRecommended() != null && f.getIsRecommended() ? 1 : 0).sum();
        
        double averageRating = getAverageRatingForProperty(propertyId);
        double averageEcoRating = getAverageEcoRatingForProperty(propertyId);
        
        return new FeedbackStatistics(totalCount, verifiedCount, recommendedCount, 
                                    averageRating, averageEcoRating);
    }
    
    /**
     * Inner class for feedback statistics
     */
    public static class FeedbackStatistics {
        private final long totalCount;
        private final long verifiedCount;
        private final long recommendedCount;
        private final double averageRating;
        private final double averageEcoRating;
        
        public FeedbackStatistics(long totalCount, long verifiedCount, long recommendedCount,
                                double averageRating, double averageEcoRating) {
            this.totalCount = totalCount;
            this.verifiedCount = verifiedCount;
            this.recommendedCount = recommendedCount;
            this.averageRating = averageRating;
            this.averageEcoRating = averageEcoRating;
        }
        
        public long getTotalCount() { return totalCount; }
        public long getVerifiedCount() { return verifiedCount; }
        public long getRecommendedCount() { return recommendedCount; }
        public double getAverageRating() { return averageRating; }
        public double getAverageEcoRating() { return averageEcoRating; }
        
        public double getRecommendationPercentage() {
            return totalCount > 0 ? (recommendedCount * 100.0 / totalCount) : 0.0;
        }
        
        public double getVerificationPercentage() {
            return totalCount > 0 ? (verifiedCount * 100.0 / totalCount) : 0.0;
        }
    }
}