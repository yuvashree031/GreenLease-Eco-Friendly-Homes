package com.greenlease.controller;

import com.greenlease.model.User;
import com.greenlease.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Controller for authentication (login/register)
 */
@Controller
public class AuthController {

    @Autowired
    private UserService userService;

    /**
     * Show login page
     */
    @GetMapping("/login")
    public String showLoginPage(@RequestParam(value = "error", required = false) String error,
                                @RequestParam(value = "logout", required = false) String logout,
                                Model model) {
        model.addAttribute("pageTitle", "Login - GreenLease");
        
        if (error != null) {
            model.addAttribute("error", "Invalid username or password");
        }
        
        if (logout != null) {
            model.addAttribute("message", "You have been logged out successfully");
        }
        
        return "auth/login";
    }

    /**
     * Show registration page
     */
    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("pageTitle", "Register - GreenLease");
        model.addAttribute("user", new User());
        return "auth/register";
    }

    /**
     * Process registration
     */
    @PostMapping("/register")
    public String processRegistration(@RequestParam String username,
                                     @RequestParam String password,
                                     @RequestParam String confirmPassword,
                                     @RequestParam String email,
                                     Model model) {
        try {
            // Validate password match
            if (!password.equals(confirmPassword)) {
                model.addAttribute("error", "Passwords do not match");
                model.addAttribute("user", new User());
                return "auth/register";
            }

            // Validate password length
            if (password.length() < 6) {
                model.addAttribute("error", "Password must be at least 6 characters long");
                model.addAttribute("user", new User());
                return "auth/register";
            }

            // Register the user
            userService.registerUser(username, password, email);
            
            model.addAttribute("success", "Registration successful! Please login.");
            return "redirect:/login?registered";
            
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("user", new User());
            return "auth/register";
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            model.addAttribute("user", new User());
            return "auth/register";
        }
    }
}
