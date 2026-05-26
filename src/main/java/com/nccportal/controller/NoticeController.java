package com.nccportal.controller;

import com.nccportal.dto.NoticeDTO;
import com.nccportal.entity.Notice;
import com.nccportal.service.NoticeService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for Notice board — post, view, delete notices.
 */
@Controller
@RequestMapping("/notices")
public class NoticeController {

    @Autowired private NoticeService noticeService;

    @GetMapping
    public String listNotices(Authentication authentication, Model model) {
        // Admin and Officer see all; we show all here (security handles the rest)
        model.addAttribute("notices", noticeService.getAllNotices());
        return "notices/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("noticeDTO", new NoticeDTO());
        model.addAttribute("targetRoles", Notice.TargetRole.values());
        return "notices/form";
    }

    @PostMapping("/add")
    public String addNotice(@Valid @ModelAttribute("noticeDTO") NoticeDTO dto,
                             BindingResult result,
                             Authentication authentication,
                             Model model,
                             RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("targetRoles", Notice.TargetRole.values());
            return "notices/form";
        }
        noticeService.addNotice(dto, authentication.getName());
        redirectAttributes.addFlashAttribute("successMessage", "Notice posted successfully.");
        return "redirect:/notices";
    }

    @GetMapping("/delete/{id}")
    public String deleteNotice(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        noticeService.deleteNotice(id);
        redirectAttributes.addFlashAttribute("successMessage", "Notice deleted.");
        return "redirect:/notices";
    }
}
