package com.nccportal.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "officers")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Officer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Name is required")
    @Pattern(regexp = "^[a-zA-Z ]+$", message = "Name must contain only alphabets")
    private String name;

    @Column(length = 100)
    private String designation;

    @Column(unique = true, length = 100)
    @Email(message = "Please enter a valid email")
    private String email;

    @Column(length = 10)
    @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone must be 10 digits")
    private String phone;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id")
    private Unit unit;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}
