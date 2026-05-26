package com.nccportal.controller;

import com.nccportal.entity.Officer;
import com.nccportal.service.DashboardService;
import com.nccportal.service.NoticeService;
import com.nccportal.service.OfficerService;
import com.nccportal.entity.Notice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Controller for Officer dashboard.
 * Shows unit-specific stats and upcoming events.
 */
@Controller
@RequestMapping("/officer")
public class OfficerController {

    @Autowired private OfficerService officerService;
    @Autowired private DashboardService dashboardService;
    @Autowired private NoticeService noticeService;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String username = authentication.getName();

        // Find officer profile from DB using username
        // (User → Officer link via user.id)
        // For simplicity, we load all officers and match by username
        Officer officer = officerService.getAllOfficers().stream()
                .filter(o -> o.getUser() != null &&
                             o.getUser().getUsername().equals(username))
                .findFirst()
                .orElse(null);

        if (officer != null && officer.getUnit() != null) {
            var stats = dashboardService.getOfficerStats(officer.getUnit().getId());
            model.addAttribute("stats", stats);
            model.addAttribute("unit", officer.getUnit());
        }

        model.addAttribute("officer", officer);
        model.addAttribute("notices",
                noticeService.getNoticesForRole(Notice.TargetRole.OFFICER).stream().limit(5).toList());

        return "officer/dashboard";
    }
}
