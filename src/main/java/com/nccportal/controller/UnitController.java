package com.nccportal.controller;

import com.nccportal.dto.UnitDTO;
import com.nccportal.service.CadetService;
import com.nccportal.service.UnitService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for Unit management — CRUD operations.
 */
@Controller
@RequestMapping("/units")
public class UnitController {

    @Autowired private UnitService unitService;
    @Autowired private CadetService cadetService;

    @GetMapping
    public String listUnits(Model model) {
        model.addAttribute("units", unitService.getAllUnits());
        return "units/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("unitDTO", new UnitDTO());
        model.addAttribute("formTitle", "Add New Unit");
        return "units/form";
    }

    @PostMapping("/add")
    public String addUnit(@Valid @ModelAttribute("unitDTO") UnitDTO dto,
                          BindingResult result,
                          Model model,
                          RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("formTitle", "Add New Unit");
            return "units/form";
        }
        unitService.addUnit(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Unit added successfully.");
        return "redirect:/units";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        var unit = unitService.getUnitById(id);
        UnitDTO dto = UnitDTO.builder()
                .id(unit.getId())
                .unitName(unit.getUnitName())
                .battalion(unit.getBattalion())
                .state(unit.getState())
                .district(unit.getDistrict())
                .description(unit.getDescription())
                .build();
        model.addAttribute("unitDTO", dto);
        model.addAttribute("formTitle", "Edit Unit");
        return "units/form";
    }

    @PostMapping("/edit/{id}")
    public String updateUnit(@PathVariable Long id,
                              @Valid @ModelAttribute("unitDTO") UnitDTO dto,
                              BindingResult result,
                              Model model,
                              RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("formTitle", "Edit Unit");
            return "units/form";
        }
        unitService.updateUnit(id, dto);
        redirectAttributes.addFlashAttribute("successMessage", "Unit updated successfully.");
        return "redirect:/units";
    }

    @GetMapping("/delete/{id}")
    public String deleteUnit(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        unitService.deleteUnit(id);
        redirectAttributes.addFlashAttribute("successMessage", "Unit deleted successfully.");
        return "redirect:/units";
    }

    /**
     * View all cadets belonging to a unit.
     */
    @GetMapping("/{id}/cadets")
    public String viewCadetsInUnit(@PathVariable Long id,
                                    @RequestParam(defaultValue = "0") int page,
                                    Model model) {
        var unit = unitService.getUnitById(id);
        var cadetPage = cadetService.getCadetsByUnit(id, page, 10);
        model.addAttribute("unit", unit);
        model.addAttribute("cadets", cadetPage.getContent());
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", cadetPage.getTotalPages());
        return "units/cadets";
    }
}
