package com.nccportal.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for explicit error URL routes.
 * These are separate from Thymeleaf template paths.
 */
@Controller
@RequestMapping("/error")
public class ErrorPageController {

    @GetMapping("/access-denied")
    public String accessDenied() {
        return "error/access-denied";
    }
}
