package com.nccportal.dto;

import com.nccportal.entity.Camp;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

/**
 * DTO for Add/Edit Camp form.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CampDTO {

    private Long id;

    @NotBlank(message = "Camp name is required")
    private String campName;

    @NotNull(message = "Camp type is required")
    private Camp.CampType type;

    @NotNull(message = "Start date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startDate;

    @NotNull(message = "End date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endDate;

    private String location;
    private String description;
    private Integer maxCadets;
}
