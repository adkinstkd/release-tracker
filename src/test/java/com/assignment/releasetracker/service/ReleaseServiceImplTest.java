package com.assignment.releasetracker.service;

import com.assignment.releasetracker.api.v1.dto.ReleaseResponseDTO;
import com.assignment.releasetracker.dto.ReleaseRequestDTO;
import com.assignment.releasetracker.dto.ReleaseStatus;
import com.assignment.releasetracker.entity.Issue;
import com.assignment.releasetracker.entity.Release;
import com.assignment.releasetracker.exception.ReleaseNotFoundException;
import com.assignment.releasetracker.repository.ReleaseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * {@link ReleaseServiceImpl} tests
 */
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class ReleaseServiceImplTest {

    private static final String RELEASE_NAME = "release 1";
    private static final String DESCRIPTION = "some desc";
    private static final String RELEASE_DATE = "2023-06-01 07:05";
    private static final String RELEASE_NOT_FOUND = "Release with id: 123 not found!";
    private static final String RELEASE_NOT_FOUND_EXCEPTION_ERROR = "ReleaseNotFoundException error was expected";
    private static final String RELEASE_ID = "123";
    private static final String RELEASE_NAME2 = "release 2";

    @Autowired
    private ReleaseRepository releaseRepository;

    @Autowired
    private ReleaseService releaseService;

    @BeforeEach
    void cleanup() {
        releaseRepository.deleteAll();
    }

    @Test
    void findReleases() {
        // given
        var status = ReleaseStatus.DONE;
        Release release = Release.builder()
                .name(RELEASE_NAME)
                .description(DESCRIPTION)
                .status(status.getValue())
                .build();
        releaseRepository.save(release);

        // when
        List<ReleaseResponseDTO> results = releaseService.findReleases(null, null);

        // then
        assertThat(results).hasSize(1);
        assertThat(results).extracting(ReleaseResponseDTO::getName).contains(RELEASE_NAME);
        assertThat(results).extracting(ReleaseResponseDTO::getDescription).contains(DESCRIPTION);
        assertThat(results).extracting(ReleaseResponseDTO::getStatus).contains(status);
    }

    @Test
    void findReleasesByStatus() {
        // given
        var status = ReleaseStatus.DONE;
        Release release = Release.builder()
                .name(RELEASE_NAME)
                .description(DESCRIPTION)
                .status(status.getValue())
                .build();
        releaseRepository.save(release);
        var name2 = RELEASE_NAME2;
        var status2 = ReleaseStatus.ON_STAGING;
        Release release2 = Release.builder()
                .name(name2)
                .description(DESCRIPTION)
                .status(status2.getValue())
                .build();
        releaseRepository.save(release2);

        // when
        List<ReleaseResponseDTO> results = releaseService.findReleases(status2.getValue(), "");

        // then
        assertThat(results).hasSize(1);
        assertThat(results).extracting(ReleaseResponseDTO::getName).contains(name2);
        assertThat(results).extracting(ReleaseResponseDTO::getDescription).contains(DESCRIPTION);
        assertThat(results).extracting(ReleaseResponseDTO::getStatus).contains(status2);
    }

    @Test
    void findReleasesByIssueName() {
        // given
        var status = ReleaseStatus.DONE;
        Release release = Release.builder()
                .name(RELEASE_NAME)
                .description(DESCRIPTION)
                .status(status.getValue())
                .build();
        releaseRepository.save(release);

        var issueName = "some problem";
        Issue issue = Issue.builder()
                .name(issueName)
                .description(DESCRIPTION)
                .status(status.getValue())
                .build();

        var name2 = RELEASE_NAME2;
        var status2 = ReleaseStatus.ON_STAGING;
        Release release2 = Release.builder()
                .name(name2)
                .description(DESCRIPTION)
                .status(status2.getValue())
                .issues(Collections.singletonList(issue))
                .build();
        releaseRepository.save(release2);

        // when
        List<ReleaseResponseDTO> results = releaseService.findReleases(null, issueName);

        // then
        assertThat(results).hasSize(1);
        assertThat(results).extracting(ReleaseResponseDTO::getName).contains(name2);
        assertThat(results).extracting(ReleaseResponseDTO::getDescription).contains(DESCRIPTION);
        assertThat(results).extracting(ReleaseResponseDTO::getStatus).contains(status2);
        assertThat(results).extracting(ReleaseResponseDTO::getIssues).isNotEmpty();
    }

    @Test
    void findReleaseById() {
        // given
        var status = ReleaseStatus.DONE;
        Release release = Release.builder()
                .name(RELEASE_NAME)
                .description(DESCRIPTION)
                .status(status.getValue())
                .build();
        Release newRelease = releaseRepository.save(release);

        // when
        ReleaseResponseDTO result = releaseService.findReleaseById(String.valueOf(newRelease.getId()));

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(RELEASE_NAME);
        assertThat(result.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(result.getStatus()).isEqualTo(status);
    }

    @Test
    void findReleaseByIdNotFound() {
        // when
        ReleaseNotFoundException thrown = assertThrows(ReleaseNotFoundException.class,
                () -> releaseService.findReleaseById(RELEASE_ID), RELEASE_NOT_FOUND_EXCEPTION_ERROR);

        // then
        assertEquals(RELEASE_NOT_FOUND, thrown.getMessage());
    }

    @Test
    void createRelease() {
        // given
        var status = ReleaseStatus.DONE;
        var releaseRequest = new ReleaseRequestDTO(RELEASE_NAME, DESCRIPTION, status, RELEASE_DATE);

        // when
        ReleaseResponseDTO result = releaseService.createRelease(releaseRequest);

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(RELEASE_NAME);
        assertThat(result.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(result.getStatus()).isEqualTo(status);

        // and
        ReleaseResponseDTO newRelease = releaseService.findReleaseById(String.valueOf(result.getId()));
        assertThat(newRelease).isNotNull();
        assertThat(newRelease.getName()).isEqualTo(RELEASE_NAME);
        assertThat(newRelease.getDescription()).isEqualTo(DESCRIPTION);
        assertThat(newRelease.getStatus()).isEqualTo(status);
        assertThat(newRelease.getCreatedAt()).isNotNull();
        assertThat(newRelease.getLastUpdateAt()).isNotNull();
        assertThat(newRelease.getReleaseDate()).isNotNull();
    }

    @Test
    void updateRelease() {
        // given
        var status = ReleaseStatus.DONE;
        var releaseRequest = new ReleaseRequestDTO(RELEASE_NAME, DESCRIPTION, status, RELEASE_DATE);
        ReleaseResponseDTO newRelease = releaseService.createRelease(releaseRequest);

        var id = newRelease.getId();
        var description2 = "some desc 2";
        var status2 = ReleaseStatus.QA_DONE_ON_STAGING;
        var releaseRequest2 = new ReleaseRequestDTO(RELEASE_NAME2, description2, status2, RELEASE_DATE);

        // when
        ReleaseResponseDTO result = releaseService.updateRelease(releaseRequest2, String.valueOf(id));

        // then
        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo(RELEASE_NAME2);
        assertThat(result.getDescription()).isEqualTo(description2);
        assertThat(result.getStatus()).isEqualTo(status2);
        assertThat(result.getCreatedAt()).isNotNull();
        assertThat(result.getLastUpdateAt()).isNotNull();
        assertThat(result.getLastUpdateAt()).isAfter(result.getCreatedAt());
        assertThat(result.getReleaseDate()).isNotNull();
    }

    @Test
    void updateReleaseNotFound() {
        // when
        ReleaseNotFoundException thrown = assertThrows(ReleaseNotFoundException.class,
                () -> releaseService.updateRelease(new ReleaseRequestDTO(), RELEASE_ID),
                RELEASE_NOT_FOUND_EXCEPTION_ERROR);

        // then
        assertEquals(RELEASE_NOT_FOUND, thrown.getMessage());
    }

    @Test
    void deleteRelease() {
        // given
        var releaseRequest = new ReleaseRequestDTO(RELEASE_NAME, DESCRIPTION, ReleaseStatus.DONE, RELEASE_DATE);
        ReleaseResponseDTO newRelease = releaseService.createRelease(releaseRequest);
        assertThat(newRelease).isNotNull();

        var id = newRelease.getId();

        // when
        releaseService.deleteRelease(String.valueOf(id));

        // then
        assertThat(releaseRepository.findAll()).isEmpty();
    }

    @Test
    void deleteReleaseNotFound() {
        // when
        ReleaseNotFoundException thrown = assertThrows(ReleaseNotFoundException.class,
                () -> releaseService.deleteRelease(RELEASE_ID),
                RELEASE_NOT_FOUND_EXCEPTION_ERROR);

        // then
        assertEquals(RELEASE_NOT_FOUND, thrown.getMessage());
    }

}
