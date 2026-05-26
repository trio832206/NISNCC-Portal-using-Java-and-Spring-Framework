package com.nccportal.controller;

import com.nccportal.dto.DashboardStatsDTO;
import com.nccportal.dto.OfficerDTO;
import com.nccportal.service.DashboardService;
import com.nccportal.service.NoticeService;
import com.nccportal.service.OfficerService;
import com.nccportal.service.UnitService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for Admin dashboard and officer management.
 * All routes secured to ADMIN role via SecurityConfig.
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private DashboardService dashboardService;
    @Autowired
    private OfficerService officerService;
    @Autowired
    private UnitService unitService;
    @Autowired
    private NoticeService noticeService;

    /**
     * Admin Dashboard — shows portal statistics.
     */
    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        DashboardStatsDTO stats = dashboardService.getAdminStats();
        model.addAttribute("stats", stats);
        model.addAttribute("recentNotices", noticeService.getAllNotices().stream().limit(5).toList());
        return "admin/dashboard";
    }

    // ---- Officer Management ----

    @GetMapping("/officers")
    public String listOfficers(Model model) {
        model.addAttribute("officers", officerService.getAllOfficers());
        return "admin/officers";
    }

    @GetMapping("/officers/add")
    public String showAddOfficerForm(Model model) {
        model.addAttribute("officerDTO", new OfficerDTO());
        model.addAttribute("units", unitService.getAllUnits());
        return "admin/officer-form";
    }

    @PostMapping("/officers/add")
    public String addOfficer(@Valid @ModelAttribute("officerDTO") OfficerDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("units", unitService.getAllUnits());
            return "admin/officer-form";
        }
        officerService.addOfficer(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Officer added successfully.");
        return "redirect:/admin/officers";
    }

    @GetMapping("/officers/edit/{id}")
    public String showEditOfficerForm(@PathVariable Long id, Model model) {
        var officer = officerService.getOfficerById(id);
        OfficerDTO dto = OfficerDTO.builder()
                .id(officer.getId())
                .name(officer.getName())
                .designation(officer.getDesignation())
                .email(officer.getEmail())
                .phone(officer.getPhone())
                .unitId(officer.getUnit() != null ? officer.getUnit().getId() : null)
                .build();
        model.addAttribute("officerDTO", dto);
        model.addAttribute("units", unitService.getAllUnits());
        return "admin/officer-form";
    }

    @PostMapping("/officers/edit/{id}")
    public String updateOfficer(@PathVariable Long id,
            @Valid @ModelAttribute("officerDTO") OfficerDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("units", unitService.getAllUnits());
            return "admin/officer-form";
        }
        officerService.updateOfficer(id, dto);
        redirectAttributes.addFlashAttribute("successMessage", "Officer updated successfully.");
        return "redirect:/admin/officers";
    }

    @GetMapping("/officers/delete/{id}")
    public String deleteOfficer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        officerService.deleteOfficer(id);
        redirectAttributes.addFlashAttribute("successMessage", "Officer deleted successfully.");
        return "redirect:/admin/officers";
    }
}
