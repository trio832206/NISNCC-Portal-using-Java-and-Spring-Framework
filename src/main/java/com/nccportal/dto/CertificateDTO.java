package com.nccportal.dto;

import com.nccportal.entity.Certificate;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

/**
 * DTO for certificate result entry form.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CertificateDTO {

    private Long id;

    @NotNull(message = "Cadet is required")
    private Long cadetId;

    @NotNull(message = "Certificate type is required")
    private Certificate.CertificateType type;

    @NotNull(message = "Result is required")
    private Certificate.CertificateResult result;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate examDate;

    private String remarks;
}
