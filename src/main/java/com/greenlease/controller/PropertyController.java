package com.greenlease.controller;

import com.greenlease.model.Property;
import com.greenlease.service.PropertyService;
import com.greenlease.service.FeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/")
public class PropertyController {
    
    @Autowired
    private PropertyService propertyService;
    
    @Autowired
    private FeedbackService feedbackService;
    
    /**
     * Redirect /greenlease to home page
     */
    @GetMapping("/greenlease")
    public String redirectToHome() {
        return "redirect:/";
    }
    
    /**
     * Home page - displays featured eco-friendly properties
     */
    @GetMapping("/")
    public String home(Model model) {
        List<Property> featuredProperties = propertyService.getEcoExcellentProperties();
        PropertyService.EcoStatistics stats = propertyService.getEcoStatistics();
        
        model.addAttribute("featuredProperties", featuredProperties);
        model.addAttribute("stats", stats);
        model.addAttribute("pageTitle", "GreenLease - Find Your Eco-Friendly Home");
        
        return "index";
    }

    @GetMapping("/properties")
    public String listProperties(
            @RequestParam(value = "city", required = false) String city,
            @RequestParam(value = "minRent", required = false) BigDecimal minRent,
            @RequestParam(value = "maxRent", required = false) BigDecimal maxRent,
            @RequestParam(value = "minEcoScore", required = false) Double minEcoScore,
            @RequestParam(value = "maxEcoScore", required = false) Double maxEcoScore,
            @RequestParam(value = "solarPanels", required = false) Boolean solarPanels,
            Model model) {
        
        List<Property> properties;
        
        // Apply filters based on parameters
        if (city != null && !city.trim().isEmpty()) {
            properties = propertyService.searchByCity(city);
            model.addAttribute("searchCity", city);
        } else if (minRent != null && maxRent != null) {
            properties = propertyService.filterByRent(minRent, maxRent);
            model.addAttribute("minRent", minRent);
            model.addAttribute("maxRent", maxRent);
        } else if (minEcoScore != null && maxEcoScore != null) {
            properties = propertyService.filterByEcoScore(minEcoScore, maxEcoScore);
            model.addAttribute("minEcoScore", minEcoScore);
            model.addAttribute("maxEcoScore", maxEcoScore);
        } else if (solarPanels != null) {
            properties = propertyService.filterBySolarPanels(solarPanels);
            model.addAttribute("solarFilter", solarPanels);
        } else {
            properties = propertyService.getAvailableProperties();
        }
        
        model.addAttribute("properties", properties);
        model.addAttribute("pageTitle", "Browse Properties - GreenLease");
        
        return "properties/list";
    }

    @GetMapping("/properties/{id}")
    public String viewProperty(@PathVariable Long id, Model model) {
        Optional<Property> propertyOpt = propertyService.getPropertyById(id);
        
        if (propertyOpt.isEmpty()) {
            return "redirect:/properties?error=notfound";
        }
        
        Property property = propertyOpt.get();
        var feedbacks = feedbackService.getFeedbackByPropertyId(id);
        var feedbackStats = feedbackService.getFeedbackStatistics(id);
        
        model.addAttribute("property", property);
        model.addAttribute("feedbacks", feedbacks);
        model.addAttribute("feedbackStats", feedbackStats);
        model.addAttribute("pageTitle", property.getTitle() + " - GreenLease");
        
        return "properties/detail";
    }
    

    @GetMapping("/properties/add")
    public String addPropertyForm(Model model) {
        model.addAttribute("property", new Property());
        model.addAttribute("pageTitle", "Add Property - GreenLease");
        return "properties/add";
    }
    

    @PostMapping("/properties/add")
    public String addProperty(@ModelAttribute Property property, Model model) {
        try {
            // Set default values if not provided
            if (property.getLandlordId() == null) {
                property.setLandlordId(1L); // Default landlord for demo
            }
            
            Property savedProperty = propertyService.saveProperty(property);
            return "redirect:/properties/" + savedProperty.getId() + "?success=added";
        } catch (Exception e) {
            model.addAttribute("error", "Failed to add property: " + e.getMessage());
            model.addAttribute("property", property);
            return "properties/add";
        }
    }
    

    @GetMapping("/search/eco")
    public String searchByEcoRating(
            @RequestParam(value = "rating", defaultValue = "excellent") String rating,
            Model model) {
        
        List<Property> properties;
        String pageTitle;
        
        switch (rating.toLowerCase()) {
            case "excellent":
                properties = propertyService.filterByEcoScore(8.0, 10.0);
                pageTitle = "Excellent Eco-Rating Properties (8.0-10.0) - GreenLease";
                break;
            case "good":
                properties = propertyService.filterByEcoScore(6.0, 7.9);
                pageTitle = "Good Eco-Rating Properties (6.0-7.9) - GreenLease";
                break;
            case "fair":
                properties = propertyService.filterByEcoScore(4.0, 5.9);
                pageTitle = "Fair Eco-Rating Properties (4.0-5.9) - GreenLease";
                break;
            default:
                properties = propertyService.getAvailableProperties();
                pageTitle = "All Properties - GreenLease";
        }
        
        model.addAttribute("properties", properties);
        model.addAttribute("pageTitle", pageTitle);
        
        return "properties/list";
    }
    

    @GetMapping("/api/search/cities")
    @ResponseBody
    public List<String> getCitySuggestions(@RequestParam String query) {
        // This would typically query a cities database
        // For demo purposes, return sample cities
        List<String> cities = List.of(
            "San Francisco", "Los Angeles", "New York", "Chicago", 
            "Seattle", "Portland", "Austin", "Denver", "Boston"
        );
        
        return cities.stream()
                .filter(city -> city.toLowerCase().contains(query.toLowerCase()))
                .limit(5)
                .toList();
    }
}