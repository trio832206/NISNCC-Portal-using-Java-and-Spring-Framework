package com.nccportal.dto;

import lombok.*;

/**
 * DTO to carry aggregated statistics for dashboard display.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsDTO {

    // Admin dashboard stats
    private long totalCadets;
    private long totalUnits;
    private long totalOfficers;
    private long upcomingCamps;
    private long totalNotices;

    // Officer dashboard stats
    private long cadetsInUnit;
    private long attendanceToday;

    // Cadet dashboard stats
    private double attendancePercentage;
    private long certificatesEarned;
    private long campsAttended;
}
