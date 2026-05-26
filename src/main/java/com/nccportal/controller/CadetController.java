package com.nccportal.controller;

import com.nccportal.entity.Cadet;
import com.nccportal.entity.Notice;
import com.nccportal.service.AttendanceService;
import com.nccportal.service.CadetService;
import com.nccportal.service.CertificateService;
import com.nccportal.service.CampService;
import com.nccportal.service.NoticeService;
import com.nccportal.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import java.time.LocalDate;

/**
 * Controller for Cadet's personal dashboard and profile.
 * Cadets can only view their own data.
 */
@Controller
@RequestMapping("/cadet")
public class CadetController {

    @Autowired private CadetService cadetService;
    @Autowired private AttendanceService attendanceService;
    @Autowired private CertificateService certificateService;
    @Autowired private CampService campService;
    @Autowired private NoticeService noticeService;
    @Autowired private UserRepository userRepository;

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model) {
        String username = authentication.getName();
        var user = userRepository.findByUsername(username).orElse(null);

        if (user != null) {
            var cadetOpt = cadetService.getCadetByUserId(user.getId());
            if (cadetOpt.isPresent()) {
                Cadet cadet = cadetOpt.get();
                model.addAttribute("cadet", cadet);

                // Attendance % for current year
                double attPct = attendanceService.calculateAttendancePercentage(
                        cadet.getId(),
                        LocalDate.of(LocalDate.now().getYear(), 1, 1),
                        LocalDate.now());
                model.addAttribute("attendancePercentage", attPct);

                // Certificates
                var certs = certificateService.getCertificatesByCadet(cadet.getId());
                model.addAttribute("certificates", certs);

                // Camp registrations
                var camps = campService.getRegistrationsByCadet(cadet.getId());
                model.addAttribute("campRegistrations", camps);
            }
        }

        // Notices visible to cadets
        model.addAttribute("notices",
                noticeService.getNoticesForRole(Notice.TargetRole.CADET).stream().limit(5).toList());

        return "cadet/dashboard";
    }

    @GetMapping("/profile")
    public String profile(Authentication authentication, Model model) {
        String username = authentication.getName();
        var user = userRepository.findByUsername(username).orElse(null);

        if (user != null) {
            var cadetOpt = cadetService.getCadetByUserId(user.getId());
            cadetOpt.ifPresent(c -> model.addAttribute("cadet", c));
        }
        return "cadet/profile";
    }
}
