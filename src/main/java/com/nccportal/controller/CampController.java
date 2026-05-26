package com.nccportal.controller;

import com.nccportal.dto.CampDTO;
import com.nccportal.entity.Camp;
import com.nccportal.service.CadetService;
import com.nccportal.service.CampService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for Camp management — CRUD and cadet registration.
 */
@Controller
@RequestMapping("/camps")
public class CampController {

    @Autowired private CampService campService;
    @Autowired private CadetService cadetService;

    @GetMapping
    public String listCamps(Model model) {
        model.addAttribute("camps", campService.getAllCamps());
        return "camps/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("campDTO", new CampDTO());
        model.addAttribute("campTypes", Camp.CampType.values());
        model.addAttribute("formTitle", "Add New Camp");
        return "camps/form";
    }

    @PostMapping("/add")
    public String addCamp(@Valid @ModelAttribute("campDTO") CampDTO dto,
                          BindingResult result,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("campTypes", Camp.CampType.values());
            model.addAttribute("formTitle", "Add New Camp");
            return "camps/form";
        }
        campService.addCamp(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Camp added successfully.");
        return "redirect:/camps";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        var camp = campService.getCampById(id);
        CampDTO dto = CampDTO.builder()
                .id(camp.getId())
                .campName(camp.getCampName())
                .type(camp.getType())
                .startDate(camp.getStartDate())
                .endDate(camp.getEndDate())
                .location(camp.getLocation())
                .description(camp.getDescription())
                .maxCadets(camp.getMaxCadets())
                .build();
        model.addAttribute("campDTO", dto);
        model.addAttribute("campTypes", Camp.CampType.values());
        model.addAttribute("formTitle", "Edit Camp");
        return "camps/form";
    }

    @PostMapping("/edit/{id}")
    public String updateCamp(@PathVariable Long id,
                              @Valid @ModelAttribute("campDTO") CampDTO dto,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("campTypes", Camp.CampType.values());
            model.addAttribute("formTitle", "Edit Camp");
            return "camps/form";
        }
        campService.updateCamp(id, dto);
        redirectAttributes.addFlashAttribute("successMessage", "Camp updated successfully.");
        return "redirect:/camps";
    }

    @GetMapping("/delete/{id}")
    public String deleteCamp(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        campService.deleteCamp(id);
        redirectAttributes.addFlashAttribute("successMessage", "Camp deleted.");
        return "redirect:/camps";
    }

    /**
     * Show camp detail with registered cadets.
     */
    @GetMapping("/{id}/registrations")
    public String campRegistrations(@PathVariable Long id, Model model) {
        var camp = campService.getCampById(id);
        var registrations = campService.getRegistrationsByCamp(id);
        model.addAttribute("camp", camp);
        model.addAttribute("registrations", registrations);
        model.addAttribute("cadets", cadetService.getAllCadets());
        return "camps/registrations";
    }

    /**
     * Register a cadet for a camp.
     */
    @PostMapping("/{campId}/register")
    public String registerCadet(@PathVariable Long campId,
                                 @RequestParam Long cadetId,
                                 RedirectAttributes redirectAttributes) {
        campService.registerCadet(campId, cadetId);
        redirectAttributes.addFlashAttribute("successMessage", "Cadet registered for camp.");
        return "redirect:/camps/" + campId + "/registrations";
    }
}
