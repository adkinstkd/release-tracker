package com.assignment.releasetracker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@Builder
@Entity
@Table(name = "issues")
@NoArgsConstructor
@AllArgsConstructor
public class Issue implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime lastUpdateAt;

    @ManyToOne
    @JoinColumn(name = "releaseid")
    private Release release;
}
