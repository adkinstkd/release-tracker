package com.assignment.releasetracker.mapper;

import com.assignment.releasetracker.api.v1.dto.IssueResponseDTO;
import com.assignment.releasetracker.entity.Issue;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

/**
 * Issue mapper
 */
@Slf4j
public class IssueMapper {

    private IssueMapper() {}

    public static List<IssueResponseDTO> fromIssues(List<Issue> issues) {
        return Optional
                .ofNullable(issues)
                .map(Collection::stream)
                .orElseGet(Stream::empty)
                .map(IssueMapper::fromIssue)
                .toList();
    }

    public static IssueResponseDTO fromIssue(Issue issue) {
        return IssueResponseDTO.builder()
                .name(issue.getName())
                .description(issue.getDescription())
                .status(issue.getStatus())
                .createdAt(issue.getCreatedAt())
                .lastUpdateAt(issue.getLastUpdateAt())
                .build();
    }
}
