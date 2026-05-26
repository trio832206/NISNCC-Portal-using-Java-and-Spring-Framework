package com.nccportal.controller;

import com.nccportal.dto.AttendanceDTO;
import com.nccportal.entity.Attendance;
import com.nccportal.service.AttendanceService;
import com.nccportal.service.CadetService;
import com.nccportal.service.UnitService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.time.LocalDate;

/**
 * Controller for Attendance marking and reporting.
 */
@Controller
@RequestMapping("/attendance")
public class AttendanceController {

    @Autowired private AttendanceService attendanceService;
    @Autowired private CadetService cadetService;
    @Autowired private UnitService unitService;

    /**
     * Show attendance marking form.
     */
    @GetMapping("/mark")
    public String showMarkForm(Model model) {
        model.addAttribute("attendanceDTO", AttendanceDTO.builder()
                .date(LocalDate.now())
                .type(Attendance.AttendanceType.PARADE)
                .build());
        model.addAttribute("cadets", cadetService.getAllCadets());
        model.addAttribute("attendanceTypes", Attendance.AttendanceType.values());
        model.addAttribute("attendanceStatuses", Attendance.AttendanceStatus.values());
        return "attendance/mark";
    }

    /**
     * Process attendance marking.
     */
    @PostMapping("/mark")
    public String markAttendance(@Valid @ModelAttribute("attendanceDTO") AttendanceDTO dto,
                                  BindingResult result,
                                  Model model,
                                  RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("cadets", cadetService.getAllCadets());
            model.addAttribute("attendanceTypes", Attendance.AttendanceType.values());
            model.addAttribute("attendanceStatuses", Attendance.AttendanceStatus.values());
            return "attendance/mark";
        }
        attendanceService.markAttendance(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Attendance marked successfully.");
        return "redirect:/attendance/report";
    }

    /**
     * Show attendance report — filter by date and type.
     */
    @GetMapping("/report")
    public String showReport(
            @RequestParam(required = false) String date,
            @RequestParam(required = false) String type,
            Model model) {

        LocalDate reportDate = (date != null && !date.isBlank())
                ? LocalDate.parse(date) : LocalDate.now();
        Attendance.AttendanceType attType = (type != null && !type.isBlank())
                ? Attendance.AttendanceType.valueOf(type) : Attendance.AttendanceType.PARADE;

        var records = attendanceService.getAttendanceByDate(reportDate, attType);

        model.addAttribute("records", records);
        model.addAttribute("reportDate", reportDate);
        model.addAttribute("reportType", attType);
        model.addAttribute("attendanceTypes", Attendance.AttendanceType.values());
        model.addAttribute("units", unitService.getAllUnits());

        return "attendance/report";
    }

    /**
     * Delete attendance record.
     */
    @GetMapping("/delete/{id}")
    public String deleteAttendance(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        attendanceService.deleteAttendance(id);
        redirectAttributes.addFlashAttribute("successMessage", "Attendance record deleted.");
        return "redirect:/attendance/report";
    }
}
