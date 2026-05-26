package com.nccportal.controller;

import com.nccportal.service.ReportService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.ui.Model;
import java.io.IOException;

/**
 * Controller for Reports — CSV export downloads.
 */
@Controller
@RequestMapping("/reports")
public class ReportController {

    @Autowired private ReportService reportService;

    /**
     * Reports landing page.
     */
    @GetMapping
    public String reportsPage(Model model) {
        return "reports/index";
    }

    /**
     * Export all cadets as CSV download.
     */
    @GetMapping("/export/cadets")
    public void exportCadets(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=cadets_report.csv");
        reportService.exportCadetsToCSV(response.getWriter());
    }

    /**
     * Export all attendance records as CSV download.
     */
    @GetMapping("/export/attendance")
    public void exportAttendance(HttpServletResponse response) throws IOException {
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=attendance_report.csv");
        reportService.exportAttendanceToCSV(response.getWriter());
    }
}
