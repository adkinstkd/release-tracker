package com.assignment.releasetracker.service;

import com.assignment.releasetracker.api.v1.dto.ReleaseResponseDTO;
import com.assignment.releasetracker.dto.ReleaseRequestDTO;

import java.util.List;

public interface ReleaseService {

    /**
     * Finds all releases if parameters not provided or filters releases by parameters if parameters sent
     *
     * @param status {@link String}
     * @param issueName {@link String}
     * @return {@link List} of {@link ReleaseResponseDTO}
     */
    List<ReleaseResponseDTO> findReleases(String status, String issueName);

    /**
     * Finds release by release id
     *
     * @param id {@link String}
     * @return {@link ReleaseResponseDTO}
     */
    ReleaseResponseDTO findReleaseById(String id);

    /**
     * Creates a new release
     *
     * @param releaseRequest {@link ReleaseRequestDTO}
     * @return {@link ReleaseResponseDTO}
     */
    ReleaseResponseDTO createRelease(ReleaseRequestDTO releaseRequest);

    /**
     * Updates an existing release
     *
     * @param releaseRequest {@link ReleaseRequestDTO}
     * @param id {@link String}
     * @return {@link ReleaseResponseDTO}
     */
    ReleaseResponseDTO updateRelease(ReleaseRequestDTO releaseRequest, String id);

    /**
     * Deletes an existing release by id
     *
     * @param id {@link String}
     */
    void deleteRelease(String id);
}
