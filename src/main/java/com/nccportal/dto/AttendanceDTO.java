package com.nccportal.dto;

import com.nccportal.entity.Attendance;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

/**
 * DTO for marking attendance.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AttendanceDTO {

    private Long id;

    @NotNull(message = "Cadet is required")
    private Long cadetId;

    @NotNull(message = "Date is required")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;

    @NotNull(message = "Status is required")
    private Attendance.AttendanceStatus status;

    @NotNull(message = "Type is required")
    private Attendance.AttendanceType type;

    private String remarks;
}
