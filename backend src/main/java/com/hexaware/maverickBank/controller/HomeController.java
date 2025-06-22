package com.hexaware.maverickbank.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

	@GetMapping("/home")
	public String home(@AuthenticationPrincipal OAuth2User user) {
	    if (user == null) {
	        return "Not logged in";
	    }
	    // Use the correct attribute key, e.g., "name" or "email"
	    String name = user.getAttribute("name");
	    if (name == null) {
	        name = user.getAttribute("email"); // fallback to email or other attribute
	    }
	    return "Welcome, " + name;
	}

	@GetMapping("/customer")
	@PreAuthorize("hasRole('CUSTOMER')")
	public String welcomeCustomer() {
	    return "Welcome, Customer!";
	}



    @GetMapping("/logout-success")
    public String logoutSuccess() {
        return "You have been successfully logged out.";
    }
}
