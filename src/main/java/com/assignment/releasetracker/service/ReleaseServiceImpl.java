package com.assignment.releasetracker.service;

import com.assignment.releasetracker.api.v1.dto.ReleaseResponseDTO;
import com.assignment.releasetracker.dto.ReleaseRequestDTO;
import com.assignment.releasetracker.entity.Release;
import com.assignment.releasetracker.exception.ReleaseNotFoundException;
import com.assignment.releasetracker.mapper.ReleaseMapper;
import com.assignment.releasetracker.repository.ReleaseRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static com.assignment.releasetracker.mapper.ReleaseMapper.toRelease;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReleaseServiceImpl implements ReleaseService {

    private static final String RELEASE_WITH_ID = "Release with id: ";
    private static final String NOT_FOUND = " not found!";

    private final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    private final ReleaseRepository releaseRepository;

    @Override
    public List<ReleaseResponseDTO> findReleases(String status, String issueName) {
        if ((status == null || status.isBlank()) && (issueName == null || issueName.isBlank())) {
            log.debug("Fetching all releases");
            return ReleaseMapper.fromReleases(releaseRepository.findAll());
        }
        log.debug("Searching for releases by release status: {} and/or issue name: {}", status, issueName);
        return ReleaseMapper.fromReleases(releaseRepository.findAllByStatusOrIssuesName(status, issueName));
    }

    @Override
    public ReleaseResponseDTO findReleaseById(String id) {
        log.debug("Searching for release by id: {}", id);
        Optional<Release> foundRelease = releaseRepository.findById(Long.valueOf(id));
        if (foundRelease.isPresent()) {
            return ReleaseMapper.fromRelease(foundRelease.get());
        }
        throw new ReleaseNotFoundException(RELEASE_WITH_ID + id + NOT_FOUND);
    }

    @Override
    public ReleaseResponseDTO createRelease(ReleaseRequestDTO releaseRequest) {
        log.debug("Creating a new release with following data: {}", releaseRequest);
        return ReleaseMapper.fromRelease(releaseRepository.save(toRelease(releaseRequest)));
    }

    @Override
    public ReleaseResponseDTO updateRelease(ReleaseRequestDTO releaseRequest, String id) {
        log.debug("Searching for release by id: {}", id);
        Optional<Release> foundRelease = releaseRepository.findById(Long.valueOf(id));
        if (foundRelease.isPresent()) {
            Release release = foundRelease.get();
            release.setName(releaseRequest.getName());
            release.setDescription(releaseRequest.getDescription());
            release.setStatus(releaseRequest.getStatus().getValue());
            release.setReleaseDate(LocalDateTime.parse(releaseRequest.getReleaseDate(), DATE_TIME_FORMATTER));
            release.setLastUpdateAt(LocalDateTime.now());
            log.debug("Updating release with following data: {}", releaseRequest);
            return ReleaseMapper.fromRelease(releaseRepository.save(release));
        }
        throw new ReleaseNotFoundException(RELEASE_WITH_ID + id + NOT_FOUND);
    }

    @Override
    public void deleteRelease(String id) {
        Optional<Release> foundRelease = releaseRepository.findById(Long.valueOf(id));
        if (foundRelease.isPresent()) {
            log.debug("Deleting release with id: {}", id);
            releaseRepository.deleteById(Long.valueOf(id));
        }
        else {
            throw new ReleaseNotFoundException(RELEASE_WITH_ID + id + NOT_FOUND);
        }
    }
}
