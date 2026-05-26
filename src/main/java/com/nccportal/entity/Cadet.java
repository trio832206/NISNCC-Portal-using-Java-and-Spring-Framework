package com.nccportal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

/**
 * Cadet entity — core entity representing an NCC cadet.
 * Linked to a User account and a Unit.
 */
@Entity
@Table(name = "cadets")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cadet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "cadet_id", unique = true, length = 20)
    private String cadetId; // e.g., NCC/TN/2024/001

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name must contain only alphabets")
    private String name;

    @Column(nullable = false)
    @NotNull(message = "Date of birth is required")
    @Past(message = "DOB must be a past date")
    private LocalDate dob;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Gender gender;

    @Column(name = "father_name", length = 100)
    @Pattern(regexp = "^[a-zA-Z ]*$", message = "Father name must contain only alphabets")
    private String fatherName;

    @Column(unique = true, length = 100)
    @Email(message = "Please enter a valid email address")
    private String email;

    @Column(length = 10)
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone must be a valid 10-digit Indian mobile number")
    private String phone;

    @Column(length = 300)
    private String address;

    @Column(length = 150)
    private String college; // College or School name

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @Enumerated(EnumType.STRING)
    @Column(name = "cadet_rank")
    private Rank rank;

    @Column(name = "enrollment_date")
    private LocalDate enrollmentDate;

    @Column(name = "blood_group", length = 5)
    private String bloodGroup; // A+, B+, O+, AB+, etc.

    @Column(name = "aadhaar_masked", length = 20)
    private String aadhaarMasked; // Stored as XXXX-XXXX-1234

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    // Enum for cadet rank
    public enum Rank {
        CADET, LANCE_CORPORAL, CORPORAL, SERGEANT, UNDER_OFFICER, SENIOR_UNDER_OFFICER
    }

    // Enum for gender
    public enum Gender {
        MALE, FEMALE, OTHER
    }
}
