package com.nccportal.exception;

import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;

/**
 * Global exception handler — catches all exceptions and maps them
 * to user-friendly error pages.
 *
 * Uses @ControllerAdvice to intercept across all controllers.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    // --- Resource Not Found (404) ---
    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleResourceNotFound(ResourceNotFoundException ex, Model model) {
        model.addAttribute("errorTitle", "Record Not Found");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("statusCode", 404);
        return "error/404";
    }

    // --- Duplicate Record (409 Conflict) ---
    @ExceptionHandler(DuplicateRecordException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public String handleDuplicate(DuplicateRecordException ex, Model model) {
        model.addAttribute("errorTitle", "Duplicate Entry");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("statusCode", 409);
        return "error/500";
    }

    // --- Invalid Input (400) ---
    @ExceptionHandler(InvalidInputException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleInvalidInput(InvalidInputException ex, Model model) {
        model.addAttribute("errorTitle", "Invalid Input");
        model.addAttribute("errorMessage", ex.getMessage());
        model.addAttribute("statusCode", 400);
        return "error/500";
    }

    // --- Unauthorized Access (403) ---
    @ExceptionHandler(UnauthorizedAccessException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public String handleUnauthorized(UnauthorizedAccessException ex, Model model) {
        model.addAttribute("errorTitle", "Access Denied");
        model.addAttribute("errorMessage", ex.getMessage());
        return "error/access-denied";
    }

    // --- Database Error (500) ---
    @ExceptionHandler(DatabaseException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleDatabaseError(DatabaseException ex, Model model) {
        model.addAttribute("errorTitle", "Database Error");
        model.addAttribute("errorMessage", "A database error occurred. Please try again later.");
        model.addAttribute("statusCode", 500);
        return "error/500";
    }

    // --- Bean Validation errors ---
    @ExceptionHandler({MethodArgumentNotValidException.class, BindException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleValidation(Exception ex, Model model) {
        model.addAttribute("errorTitle", "Validation Failed");
        model.addAttribute("errorMessage", "Please check your input and try again.");
        model.addAttribute("statusCode", 400);
        return "error/500";
    }

    // --- 404 No Handler Found ---
    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNoHandler(NoHandlerFoundException ex, Model model) {
        model.addAttribute("errorTitle", "Page Not Found");
        model.addAttribute("errorMessage", "The page you are looking for does not exist.");
        model.addAttribute("statusCode", 404);
        return "error/404";
    }

    // --- Generic fallback (500) ---
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGeneral(Exception ex, Model model) {
        model.addAttribute("errorTitle", "Unexpected Error");
        model.addAttribute("errorMessage", "An unexpected error occurred. Please contact the administrator.");
        model.addAttribute("statusCode", 500);
        return "error/500";
    }
}
