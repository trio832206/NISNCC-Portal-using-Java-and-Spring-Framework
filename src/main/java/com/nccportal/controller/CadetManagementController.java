package com.nccportal.controller;

import com.nccportal.dto.CadetDTO;
import com.nccportal.entity.Cadet;
import com.nccportal.service.CadetService;
import com.nccportal.service.UnitService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for Cadet management — CRUD, search, filter, pagination.
 * Accessible by ADMIN and OFFICER roles.
 */
@Controller
@RequestMapping("/cadets")
public class CadetManagementController {

    @Autowired private CadetService cadetService;
    @Autowired private UnitService unitService;

    /**
     * List all cadets with optional search, pagination, and sorting.
     */
    @GetMapping
    public String listCadets(
            @RequestParam(defaultValue = "") String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "name") String sortField,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) Long unitId,
            Model model) {

        Page<Cadet> cadetPage;

        if (search != null && !search.isBlank()) {
            cadetPage = cadetService.searchCadets(search, page, size);
        } else if (unitId != null) {
            cadetPage = cadetService.getCadetsByUnit(unitId, page, size);
        } else {
            cadetPage = cadetService.getAllCadets(page, size, sortField, sortDir);
        }

        model.addAttribute("cadets", cadetPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", cadetPage.getTotalPages());
        model.addAttribute("totalElements", cadetPage.getTotalElements());
        model.addAttribute("search", search);
        model.addAttribute("sortField", sortField);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("units", unitService.getAllUnits());
        model.addAttribute("selectedUnitId", unitId);

        return "cadets/list";
    }

    /**
     * Show add cadet form.
     */
    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("cadetDTO", new CadetDTO());
        model.addAttribute("units", unitService.getAllUnits());
        model.addAttribute("ranks", Cadet.Rank.values());
        model.addAttribute("genders", Cadet.Gender.values());
        model.addAttribute("formTitle", "Add New Cadet");
        return "cadets/form";
    }

    /**
     * Process add cadet form submission.
     */
    @PostMapping("/add")
    public String addCadet(@Valid @ModelAttribute("cadetDTO") CadetDTO dto,
                            BindingResult result,
                            Model model,
                            RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("units", unitService.getAllUnits());
            model.addAttribute("ranks", Cadet.Rank.values());
            model.addAttribute("genders", Cadet.Gender.values());
            model.addAttribute("formTitle", "Add New Cadet");
            return "cadets/form";
        }
        cadetService.addCadet(dto);
        redirectAttributes.addFlashAttribute("successMessage",
                "Cadet added successfully. Default password: cadet123");
        return "redirect:/cadets";
    }

    /**
     * Show edit cadet form.
     */
    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Cadet cadet = cadetService.getCadetById(id);
        CadetDTO dto = CadetDTO.builder()
                .id(cadet.getId())
                .cadetId(cadet.getCadetId())
                .name(cadet.getName())
                .dob(cadet.getDob())
                .gender(cadet.getGender())
                .fatherName(cadet.getFatherName())
                .email(cadet.getEmail())
                .phone(cadet.getPhone())
                .address(cadet.getAddress())
                .college(cadet.getCollege())
                .unitId(cadet.getUnit() != null ? cadet.getUnit().getId() : null)
                .rank(cadet.getRank())
                .enrollmentDate(cadet.getEnrollmentDate())
                .bloodGroup(cadet.getBloodGroup())
                .build();

        model.addAttribute("cadetDTO", dto);
        model.addAttribute("units", unitService.getAllUnits());
        model.addAttribute("ranks", Cadet.Rank.values());
        model.addAttribute("genders", Cadet.Gender.values());
        model.addAttribute("formTitle", "Edit Cadet");
        return "cadets/form";
    }

    /**
     * Process edit cadet form.
     */
    @PostMapping("/edit/{id}")
    public String updateCadet(@PathVariable Long id,
                               @Valid @ModelAttribute("cadetDTO") CadetDTO dto,
                               BindingResult result,
                               Model model,
                               RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("units", unitService.getAllUnits());
            model.addAttribute("ranks", Cadet.Rank.values());
            model.addAttribute("genders", Cadet.Gender.values());
            model.addAttribute("formTitle", "Edit Cadet");
            return "cadets/form";
        }
        cadetService.updateCadet(id, dto);
        redirectAttributes.addFlashAttribute("successMessage", "Cadet updated successfully.");
        return "redirect:/cadets";
    }

    /**
     * View cadet profile.
     */
    @GetMapping("/view/{id}")
    public String viewCadet(@PathVariable Long id, Model model) {
        model.addAttribute("cadet", cadetService.getCadetById(id));
        return "cadets/view";
    }

    /**
     * Delete cadet (with confirmation via JS).
     */
    @GetMapping("/delete/{id}")
    public String deleteCadet(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        cadetService.deleteCadet(id);
        redirectAttributes.addFlashAttribute("successMessage", "Cadet deleted successfully.");
        return "redirect:/cadets";
    }
}
