package com.assignment.releasetracker.api.v1.dto;

import com.assignment.releasetracker.dto.ReleaseStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReleaseResponseDTO {

    private Long id;
    private String name;
    private String description;
    private ReleaseStatus status;
    private LocalDateTime releaseDate;
    private LocalDateTime createdAt;
    private LocalDateTime lastUpdateAt;
    private List<IssueResponseDTO> issues;
}
