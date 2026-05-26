package com.nccportal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

/**
 * Camp entity — represents NCC camps like ATC, RDC, TSC, NIC, Trekking.
 */
@Entity
@Table(name = "camps")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Camp {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "camp_name", nullable = false, length = 150)
    @NotBlank(message = "Camp name is required")
    private String campName;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CampType type;

    @Column(nullable = false)
    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @Column(nullable = false)
    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @Column(length = 200)
    private String location;

    @Column(length = 500)
    private String description;

    @Column(name = "max_cadets")
    private Integer maxCadets;

    // Types of NCC camps
    public enum CampType {
        ATC, RDC, TSC, NIC, TREKKING, OTHER
    }
}
