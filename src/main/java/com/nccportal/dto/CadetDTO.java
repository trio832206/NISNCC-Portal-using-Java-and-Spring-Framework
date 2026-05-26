package com.nccportal.dto;

import com.nccportal.entity.Cadet;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

/**
 * DTO for Add/Edit Cadet form.
 * Used to transfer form data between controller and view.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CadetDTO {

    private Long id;

    private String cadetId;

    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name must contain only alphabets")
    private String name;

    @NotNull(message = "Date of birth is required")
    @Past(message = "DOB must be a past date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dob;

    @NotNull(message = "Gender is required")
    private Cadet.Gender gender;

    @Pattern(regexp = "^[a-zA-Z ]*$", message = "Father name must contain only alphabets")
    private String fatherName;

    @Email(message = "Enter a valid email")
    @NotBlank(message = "Email is required")
    private String email;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone must be a valid 10-digit Indian mobile number")
    private String phone;

    private String address;

    private String college;

    private Long unitId;

    private Cadet.Rank rank;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate enrollmentDate;

    private String bloodGroup;

    // Raw Aadhaar input (12 digits) — will be masked before saving
    @Pattern(regexp = "^\\d{12}$", message = "Aadhaar must be 12 digits")
    private String aadhaar;
}
