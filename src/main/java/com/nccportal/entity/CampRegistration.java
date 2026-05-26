package com.nccportal.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * CampRegistration entity — tracks which cadets are registered for which camps,
 * their attendance, and performance.
 */
@Entity
@Table(name = "camp_registrations",
       uniqueConstraints = @UniqueConstraint(columnNames = {"camp_id", "cadet_id"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "camp_id", nullable = false)
    private Camp camp;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cadet_id", nullable = false)
    private Cadet cadet;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus attendance;

    @Enumerated(EnumType.STRING)
    private Performance performance;

    @Column(length = 200)
    private String remarks;

    public enum AttendanceStatus {
        REGISTERED, ATTENDED, ABSENT, WITHDRAWN
    }

    public enum Performance {
        EXCELLENT, GOOD, AVERAGE, POOR, NOT_RATED
    }
}
