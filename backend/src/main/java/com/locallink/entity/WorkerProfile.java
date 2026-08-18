package com.locallink.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "worker_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkerProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private String serviceCategory;

    private Integer experienceYears;

    @Column(length = 1000)
    private String bio;

    private String location;

    private String availability;
}