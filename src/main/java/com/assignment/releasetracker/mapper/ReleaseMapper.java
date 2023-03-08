package com.assignment.releasetracker.mapper;

import com.assignment.releasetracker.api.v1.dto.ReleaseResponseDTO;
import com.assignment.releasetracker.dto.ReleaseRequestDTO;
import com.assignment.releasetracker.dto.ReleaseStatus;
import com.assignment.releasetracker.entity.Release;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Release mapper
 */
@Slf4j
public class ReleaseMapper {

    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private ReleaseMapper() {}

    public static List<ReleaseResponseDTO> fromReleases(List<Release> releases) {
        return Optional
                .ofNullable(releases)
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .map(ReleaseMapper::fromRelease)
                .toList();
    }

    public static ReleaseResponseDTO fromRelease(Release release) {
        return ReleaseResponseDTO.builder()
                .id(release.getId())
                .name(release.getName())
                .description(release.getDescription())
                .status(ReleaseStatus.of(release.getStatus()))
                .releaseDate(release.getReleaseDate())
                .createdAt(release.getCreatedAt())
                .lastUpdateAt(release.getLastUpdateAt())
                .issues(IssueMapper.fromIssues(release.getIssues()))
                .build();
    }

    public static Release toRelease(ReleaseRequestDTO releaseRequestDTO) {
        return Release.builder()
                .name(releaseRequestDTO.getName())
                .description(releaseRequestDTO.getDescription())
                .status(releaseRequestDTO.getStatus().getValue())
                .releaseDate(LocalDateTime.parse(releaseRequestDTO.getReleaseDate(), DATE_TIME_FORMATTER))
                .createdAt(LocalDateTime.now())
                .lastUpdateAt(LocalDateTime.now())
                .build();
    }

}
