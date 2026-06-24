package com.greenlease.controller;

import com.greenlease.model.Feedback;
import com.greenlease.model.Property;
import com.greenlease.service.FeedbackService;
import com.greenlease.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

/**
 * Controller for feedback and rating functionality
 */
@Controller
@RequestMapping("/feedback")
public class FeedbackController {
    
    @Autowired
    private FeedbackService feedbackService;
    
    @Autowired
    private PropertyService propertyService;
    
    /**
     * Show feedback form for a property
     */
    @GetMapping("/add/{propertyId}")
    public String showFeedbackForm(@PathVariable Long propertyId, Model model) {
        Optional<Property> propertyOpt = propertyService.getPropertyById(propertyId);
        
        if (propertyOpt.isEmpty()) {
            return "redirect:/properties?error=notfound";
        }
        
        Property property = propertyOpt.get();
        Feedback feedback = new Feedback();
        feedback.setPropertyId(propertyId);
        
        model.addAttribute("property", property);
        model.addAttribute("feedback", feedback);
        model.addAttribute("pageTitle", "Leave Feedback - " + property.getTitle());
        
        return "feedback/add";
    }
    
    /**
     * Process feedback submission
     */
    @PostMapping("/add")
    public String submitFeedback(@ModelAttribute Feedback feedback, Model model) {
        try {
            @SuppressWarnings("unused")
            Feedback savedFeedback = feedbackService.saveFeedback(feedback);
            return "redirect:/properties/" + feedback.getPropertyId() + "?success=feedback";
        } catch (Exception e) {
            Optional<Property> propertyOpt = propertyService.getPropertyById(feedback.getPropertyId());
            if (propertyOpt.isPresent()) {
                model.addAttribute("property", propertyOpt.get());
                model.addAttribute("feedback", feedback);
                model.addAttribute("error", "Failed to submit feedback: " + e.getMessage());
                return "feedback/add";
            }
            return "redirect:/properties?error=feedback";
        }
    }
    
    /**
     * View all feedback for admin/landlord
     */
    @GetMapping("/manage")
    public String manageFeedback(
            @RequestParam(value = "propertyId", required = false) Long propertyId,
            Model model) {
        
        var feedbacks = propertyId != null ? 
            feedbackService.getFeedbackByPropertyId(propertyId) : 
            feedbackService.getAllFeedback();
            
        model.addAttribute("feedbacks", feedbacks);
        model.addAttribute("selectedPropertyId", propertyId);
        model.addAttribute("pageTitle", "Manage Feedback - GreenLease");
        
        return "feedback/manage";
    }
    
    /**
     * Verify feedback (admin function)
     */
    @PostMapping("/verify/{id}")
    public String verifyFeedback(@PathVariable Long id) {
        feedbackService.verifyFeedback(id);
        return "redirect:/feedback/manage?success=verified";
    }
    
    /**
     * Delete feedback (admin function)
     */
    @PostMapping("/delete/{id}")
    public String deleteFeedback(@PathVariable Long id) {
        feedbackService.deleteFeedback(id);
        return "redirect:/feedback/manage?success=deleted";
    }
    
    /**
     * API endpoint to get feedback statistics
     */
    @GetMapping("/api/stats/{propertyId}")
    @ResponseBody
    public FeedbackService.FeedbackStatistics getFeedbackStats(@PathVariable Long propertyId) {
        return feedbackService.getFeedbackStatistics(propertyId);
    }
}
