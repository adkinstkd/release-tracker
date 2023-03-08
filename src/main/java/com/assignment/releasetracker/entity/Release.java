package com.assignment.releasetracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Release entity
 */
@Data
@Builder
@Entity
@Table(name = "releases")
@NoArgsConstructor
@AllArgsConstructor
public class Release implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String status;

    private LocalDateTime releaseDate;

    private LocalDateTime createdAt;

    private LocalDateTime lastUpdateAt;

    // TODO: put LAZY instead of EAGER
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Issue> issues;
}
