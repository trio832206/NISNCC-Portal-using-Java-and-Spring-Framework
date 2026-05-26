package com.nccportal.controller;

import com.nccportal.dto.CertificateDTO;
import com.nccportal.entity.Certificate;
import com.nccportal.service.CadetService;
import com.nccportal.service.CertificateService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller for Certificate management — A, B, C certificates.
 */
@Controller
@RequestMapping("/certificates")
public class CertificateController {

    @Autowired private CertificateService certificateService;
    @Autowired private CadetService cadetService;

    @GetMapping
    public String listCertificates(Model model) {
        model.addAttribute("certificates", certificateService.getAllCertificates());
        return "certificates/list";
    }

    @GetMapping("/add")
    public String showAddForm(Model model) {
        model.addAttribute("certificateDTO", new CertificateDTO());
        model.addAttribute("cadets", cadetService.getAllCadets());
        model.addAttribute("certTypes", Certificate.CertificateType.values());
        model.addAttribute("certResults", Certificate.CertificateResult.values());
        return "certificates/form";
    }

    @PostMapping("/add")
    public String addCertificate(@Valid @ModelAttribute("certificateDTO") CertificateDTO dto,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("cadets", cadetService.getAllCadets());
            model.addAttribute("certTypes", Certificate.CertificateType.values());
            model.addAttribute("certResults", Certificate.CertificateResult.values());
            return "certificates/form";
        }
        certificateService.addCertificate(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Certificate record added successfully.");
        return "redirect:/certificates";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        var cert = certificateService.getCertificateById(id);
        CertificateDTO dto = CertificateDTO.builder()
                .id(cert.getId())
                .cadetId(cert.getCadet().getId())
                .type(cert.getType())
                .result(cert.getResult())
                .examDate(cert.getExamDate())
                .remarks(cert.getRemarks())
                .build();
        model.addAttribute("certificateDTO", dto);
        model.addAttribute("cadets", cadetService.getAllCadets());
        model.addAttribute("certTypes", Certificate.CertificateType.values());
        model.addAttribute("certResults", Certificate.CertificateResult.values());
        return "certificates/form";
    }

    @PostMapping("/edit/{id}")
    public String updateCertificate(@PathVariable Long id,
                                     @Valid @ModelAttribute("certificateDTO") CertificateDTO dto,
                                     BindingResult result,
                                     Model model,
                                     RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("cadets", cadetService.getAllCadets());
            model.addAttribute("certTypes", Certificate.CertificateType.values());
            model.addAttribute("certResults", Certificate.CertificateResult.values());
            return "certificates/form";
        }
        certificateService.updateCertificate(id, dto);
        redirectAttributes.addFlashAttribute("successMessage", "Certificate updated successfully.");
        return "redirect:/certificates";
    }

    @GetMapping("/delete/{id}")
    public String deleteCertificate(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        certificateService.deleteCertificate(id);
        redirectAttributes.addFlashAttribute("successMessage", "Certificate deleted.");
        return "redirect:/certificates";
    }
}
