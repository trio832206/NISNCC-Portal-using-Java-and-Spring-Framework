package com.nccportal.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;

/**
 * DTO for Add/Edit Officer form.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OfficerDTO {

    private Long id;

    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name must contain only alphabets")
    private String name;

    private String designation;

    @jakarta.validation.constraints.Email(message = "Enter a valid email")
    private String email;

    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone must be 10 digits")
    private String phone;

    private Long unitId;

    private String username;
    private String password;
}
