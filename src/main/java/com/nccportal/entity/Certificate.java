package com.nccportal.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

/**
 * Certificate entity — tracks A, B, C certificate exam results for cadets.
 */
@Entity
@Table(name = "certificates",
       uniqueConstraints = @UniqueConstraint(columnNames = {"cadet_id", "type"}))
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cadet_id", nullable = false)
    private Cadet cadet;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CertificateType type;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CertificateResult result;

    @Column(name = "exam_date")
    private LocalDate examDate;

    @Column(length = 200)
    private String remarks;

    // A, B, C Certificate types
    public enum CertificateType {
        A, B, C
    }

    // Result of the certificate exam
    public enum CertificateResult {
        ELIGIBLE, NOT_ELIGIBLE, PASSED, FAILED, PENDING
    }
}
