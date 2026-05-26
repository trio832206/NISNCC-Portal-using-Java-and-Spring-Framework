package com.nccportal.service;

import com.nccportal.dto.DashboardStatsDTO;
import com.nccportal.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;

/**
 * Service for aggregating dashboard statistics.
 */
@Service
public class DashboardService {

        @Autowired
        private CadetRepository cadetRepository;
        @Autowired
        private UnitRepository unitRepository;
        @Autowired
        private OfficerRepository officerRepository;
        @Autowired
        private CampRepository campRepository;
        @Autowired
        private NoticeRepository noticeRepository;

        /**
         * Get stats for admin dashboard.
         */
        public DashboardStatsDTO getAdminStats() {
                return DashboardStatsDTO.builder()
                                .totalCadets(cadetRepository.count())
                                .totalUnits(unitRepository.count())
                                .totalOfficers(officerRepository.count())
                                .upcomingCamps(campRepository
                                                .findByStartDateAfterOrderByStartDateAsc(LocalDate.now()).size())
                                .totalNotices(noticeRepository.count())
                                .build();
        }

        /**
         * Get stats for officer dashboard (by unit).
         */
        public DashboardStatsDTO getOfficerStats(Long unitId) {
                var unit = unitRepository.findById(unitId).orElse(null);
                long cadetsInUnit = unit != null ? cadetRepository.countByUnit(unit) : 0;
                long upcomingCamps = campRepository
                                .findByStartDateAfterOrderByStartDateAsc(LocalDate.now()).size();

                return DashboardStatsDTO.builder()
                                .cadetsInUnit(cadetsInUnit)
                                .upcomingCamps(upcomingCamps)
                                .build();
        }
}
