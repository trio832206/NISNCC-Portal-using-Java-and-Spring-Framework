package com.nccportal.dto;

import jakarta.validation.constraints.*;
import lombok.*;

/**
 * DTO for Add/Edit Unit form.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UnitDTO {

    private Long id;

    @NotBlank(message = "Unit name is required")
    private String unitName;

    @NotBlank(message = "Battalion is required")
    private String battalion;

    @NotBlank(message = "State is required")
    private String state;

    @NotBlank(message = "District is required")
    private String district;

    private String description;
}
