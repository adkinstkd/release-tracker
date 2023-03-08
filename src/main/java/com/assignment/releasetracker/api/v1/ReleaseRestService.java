package com.assignment.releasetracker.api.v1;

import com.assignment.releasetracker.api.v1.dto.ReleaseResponseDTO;
import com.assignment.releasetracker.dto.ErrorResponseDTO;
import com.assignment.releasetracker.dto.ReleaseRequestDTO;
import com.assignment.releasetracker.service.ReleaseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/v1", produces = MediaType.APPLICATION_JSON_VALUE)
//@Api(value = "/api/v1", tags = {"Release"})
@RequiredArgsConstructor
public class ReleaseRestService {

    private final ReleaseService releaseService;

    @Operation(summary = "Finds all/filters releases")
    @GetMapping("/releases")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found the releases",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReleaseResponseDTO.class))) })
    public ResponseEntity<List<ReleaseResponseDTO>> findReleases(@RequestParam(required = false) String status,
                                                                 @RequestParam(required = false) String issueName) {
        return new ResponseEntity<>(releaseService.findReleases(status, issueName), HttpStatus.OK);
    }

    @Operation(summary = "Finds release by release id")
    @GetMapping("/releases/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Found the release",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReleaseResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Release not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class))) })
    public ResponseEntity<ReleaseResponseDTO> findReleaseById(@PathVariable String id) {
        return new ResponseEntity<>(releaseService.findReleaseById(id), HttpStatus.OK);
    }

    @Operation(summary = "Creates release")
    @PostMapping("/releases")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Created the release",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReleaseResponseDTO.class)))})
    public ResponseEntity<ReleaseResponseDTO> createRelease(@RequestBody @Valid ReleaseRequestDTO releaseRequestDTO) {
        return new ResponseEntity<>(releaseService.createRelease(releaseRequestDTO),
                HttpStatus.OK);
    }

    @Operation(summary = "Updates release")
    @PutMapping("/releases/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Updated the release",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReleaseResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Release not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class))) })
    public ResponseEntity<ReleaseResponseDTO> updateRelease(@RequestBody @Valid ReleaseRequestDTO releaseRequestDTO,
                                                            @PathVariable String id) {
        return new ResponseEntity<>(releaseService.updateRelease(releaseRequestDTO, id),
                HttpStatus.OK);
    }

    @Operation(summary = "Deletes release by release id")
    @DeleteMapping("/releases/{id}")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Deleted the release",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ReleaseResponseDTO.class))),
            @ApiResponse(responseCode = "404", description = "Release not found",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponseDTO.class))) })
    public ResponseEntity<ReleaseResponseDTO> deleteReleaseById(@PathVariable String id) {
        releaseService.deleteRelease(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
