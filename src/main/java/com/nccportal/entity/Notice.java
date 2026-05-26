package com.nccportal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;

/**
 * Notice entity — bulletin board notices posted by Admin or Officers.
 * Can target specific roles or all users.
 */
@Entity
@Table(name = "notices")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    @NotBlank(message = "Title is required")
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    @NotBlank(message = "Message is required")
    private String message;

    @Column(name = "posted_by", length = 100)
    private String postedBy; // Username of the poster

    @Column(name = "posted_date", nullable = false)
    private LocalDate postedDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "target_role")
    private TargetRole targetRole; // Who can see this notice

    // ALL means visible to everyone
    public enum TargetRole {
        ALL, CADET, OFFICER, ADMIN
    }
}
