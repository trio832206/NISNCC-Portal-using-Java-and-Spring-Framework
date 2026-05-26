package com.nccportal.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

/**
 * Attendance entity — records parade or camp attendance for each cadet.
 */
@Entity
@Table(name = "attendance",
       uniqueConstraints = @UniqueConstraint(columnNames = {"cadet_id", "date", "type"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cadet_id", nullable = false)
    private Cadet cadet;

    @Column(nullable = false)
    private LocalDate date;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceType type;

    @Column(length = 200)
    private String remarks;

    // Status: Present or Absent
    public enum AttendanceStatus {
        PRESENT, ABSENT, LEAVE
    }

    // Type: Parade (regular) or Camp attendance
    public enum AttendanceType {
        PARADE, CAMP
    }
}
