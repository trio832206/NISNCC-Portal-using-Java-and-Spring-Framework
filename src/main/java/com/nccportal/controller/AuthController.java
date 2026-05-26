package com.nccportal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Controller for login/logout pages.
 * Spring Security handles the actual POST /login processing.
 */
@Controller
public class AuthController {

    /**
     * Show the login page.
     * Spring Security will handle authentication via POST /login.
     */
    @GetMapping("/login")
    public String showLoginPage() {
        return "login";
    }

    /**
     * Root URL redirect — Spring Security will redirect to role dashboard.
     */
    @GetMapping("/")
    public String root() {
        return "redirect:/login";
    }
}
