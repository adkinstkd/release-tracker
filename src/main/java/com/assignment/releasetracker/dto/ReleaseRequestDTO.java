package com.assignment.releasetracker.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ReleaseRequestDTO {

    @NotBlank(message = "name: Cannot be blank")
    private String name;

    private String description;

    private ReleaseStatus status;

    @NotBlank(message = "releaseDate: Cannot be blank")
    private String releaseDate;
}
