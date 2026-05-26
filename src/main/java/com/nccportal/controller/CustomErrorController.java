package com.nccportal.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Custom error controller — handles all /error path requests
 * and maps to appropriate error pages.
 */
@Controller
public class CustomErrorController implements ErrorController {

    @RequestMapping("/error")
    public String handleError(HttpServletRequest request, Model model) {
        // Get HTTP status code from request attributes
        Object status = request.getAttribute("jakarta.servlet.error.status_code");
        Object message = request.getAttribute("jakarta.servlet.error.message");

        int statusCode = status != null ? Integer.parseInt(status.toString()) : 500;

        model.addAttribute("statusCode", statusCode);
        model.addAttribute("errorMessage",
                message != null && !message.toString().isBlank()
                        ? message.toString()
                        : "An error occurred. Please try again.");

        return switch (statusCode) {
            case 404 -> {
                model.addAttribute("errorTitle", "Page Not Found");
                yield "error/404";
            }
            case 403 -> {
                model.addAttribute("errorTitle", "Access Denied");
                yield "error/access-denied";
            }
            default -> {
                model.addAttribute("errorTitle", "Internal Server Error");
                yield "error/500";
            }
        };
    }
}
