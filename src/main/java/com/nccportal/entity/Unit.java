package com.nccportal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

/**
 * Unit entity — represents an NCC battalion unit.
 * Each unit is managed by one officer.
 */
@Entity
@Table(name = "units")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Unit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "unit_name", nullable = false, length = 100)
    @NotBlank(message = "Unit name is required")
    private String unitName;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Battalion is required")
    private String battalion;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "State is required")
    private String state;

    @Column(nullable = false, length = 50)
    @NotBlank(message = "District is required")
    private String district;

    @Column(length = 200)
    private String description;

    @Column(name = "established_date")
    private LocalDate establishedDate;
}
