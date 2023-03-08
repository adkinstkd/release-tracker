package com.assignment.releasetracker.api.v1;

import com.assignment.releasetracker.api.v1.dto.IssueResponseDTO;
import com.assignment.releasetracker.api.v1.dto.ReleaseResponseDTO;
import com.assignment.releasetracker.dto.ReleaseRequestDTO;
import com.assignment.releasetracker.dto.ReleaseStatus;
import com.assignment.releasetracker.exception.ReleaseNotFoundException;
import com.assignment.releasetracker.exception.RestExceptionHandler;
import com.assignment.releasetracker.service.ReleaseService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Unit tests for class {@link ReleaseRestService}
 */
@WebMvcTest
@ContextConfiguration(classes = {ReleaseRestService.class, RestExceptionHandler.class})
@AutoConfigureMockMvc(addFilters = false)
class ReleaseRestServiceTest {

    private static final String RELEASE_API = "/api/v1/releases";
    private static final String RELEASE_NAME = "release 1";
    private static final String DESCRIPTION = "some desc";
    private static final String RELEASE_DATE = "2023-06-01 07:05";
    private static final String RELEASE_ID = "1";

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ReleaseService releaseService;

  @Test
  void findReleases() throws Exception {

      // given
      var status = ReleaseStatus.DONE;
      var status2 = ReleaseStatus.ON_DEV;
      var name2 = "release 2";
      var id2 = 2L;
      ReleaseResponseDTO release = ReleaseResponseDTO.builder()
              .id(Long.valueOf(RELEASE_ID))
              .name(RELEASE_NAME)
              .description(DESCRIPTION)
              .releaseDate(LocalDateTime.now().minusDays(2))
              .status(status)
              .build();
      ReleaseResponseDTO release2 = ReleaseResponseDTO.builder()
              .id(id2)
              .name(name2)
              .description(DESCRIPTION)
              .releaseDate(LocalDateTime.now().minusDays(1))
              .status(status2)
              .build();
      when(releaseService.findReleases(any(), any())).thenReturn(List.of(release, release2));

      // when/then
      mockMvc.perform(MockMvcRequestBuilders.get(RELEASE_API)
                      .accept(MediaType.APPLICATION_JSON))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$[*].id", containsInAnyOrder((Integer.parseInt(RELEASE_ID)),
                      Integer.parseInt(String.valueOf(id2)))))
              .andExpect(jsonPath("$[*].name", containsInAnyOrder(RELEASE_NAME, name2)))
              .andExpect(jsonPath("$[*].description", containsInAnyOrder(DESCRIPTION, DESCRIPTION)))
              .andExpect(jsonPath("$[*].status", containsInAnyOrder(status.name(), status2.name())));

      // and
      verify(releaseService, times(1)).findReleases(any(), any());
  }

    @Test
    void findReleasesByStatus() throws Exception {

        // given
        var status = ReleaseStatus.DONE;
        ReleaseResponseDTO release = ReleaseResponseDTO.builder()
                .id(Long.valueOf(RELEASE_ID))
                .name(RELEASE_NAME)
                .description(DESCRIPTION)
                .releaseDate(LocalDateTime.now().minusDays(2))
                .status(status)
                .build();
        when(releaseService.findReleases(status.getValue(), null)).thenReturn(Collections.singletonList(release));

        // when/then
        mockMvc.perform(MockMvcRequestBuilders.get(RELEASE_API)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("status", status.getValue()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder((Integer.parseInt(RELEASE_ID)))))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(RELEASE_NAME)))
                .andExpect(jsonPath("$[*].description", containsInAnyOrder(DESCRIPTION)))
                .andExpect(jsonPath("$[*].status", containsInAnyOrder(status.name())));

        // and
        verify(releaseService, times(1)).findReleases(status.getValue(), null);
    }

    @Test
    void findReleasesByIssueName() throws Exception {

        // given
        var issueName = "some problem";
        IssueResponseDTO issue = IssueResponseDTO.builder()
                .id(2L)
                .name(issueName)
                .description(DESCRIPTION)
                .build();
        var status = ReleaseStatus.DONE;
        ReleaseResponseDTO release = ReleaseResponseDTO.builder()
                .id(Long.valueOf(RELEASE_ID))
                .name(RELEASE_NAME)
                .description(DESCRIPTION)
                .releaseDate(LocalDateTime.now().minusDays(2))
                .status(status)
                .issues(Collections.singletonList(issue))
                .build();
        when(releaseService.findReleases(null, issueName)).thenReturn(Collections.singletonList(release));

        // when/then
        mockMvc.perform(MockMvcRequestBuilders.get(RELEASE_API)
                        .accept(MediaType.APPLICATION_JSON)
                        .param("issueName", issueName))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder((Integer.parseInt(RELEASE_ID)))))
                .andExpect(jsonPath("$[*].name", containsInAnyOrder(RELEASE_NAME)))
                .andExpect(jsonPath("$[*].description", containsInAnyOrder(DESCRIPTION)))
                .andExpect(jsonPath("$[*].issues[*].name", containsInAnyOrder(issueName)))
                .andExpect(jsonPath("$[*].issues[*].description", containsInAnyOrder(DESCRIPTION)))
                .andExpect(jsonPath("$[*].status", containsInAnyOrder(status.name())));

        // and
        verify(releaseService, times(1)).findReleases(null, issueName);
    }

  @Test
  void findReleaseById() throws Exception {

      // given
      var status = ReleaseStatus.DONE;
      ReleaseResponseDTO release = ReleaseResponseDTO.builder()
              .name(RELEASE_NAME)
              .description(DESCRIPTION)
              .releaseDate(LocalDateTime.now().minusDays(2))
              .status(status)
              .build();
      when(releaseService.findReleaseById(RELEASE_ID)).thenReturn(release);

      // when/then
      mockMvc.perform(MockMvcRequestBuilders.get(RELEASE_API + "/{id}", RELEASE_ID)
                      .accept(MediaType.APPLICATION_JSON))
              .andExpect(status().isOk())
              .andExpect(jsonPath("$.name").value(RELEASE_NAME))
              .andExpect(jsonPath("$.description").value(DESCRIPTION))
              .andExpect(jsonPath("$.status").value(status.name()));

      // and
      verify(releaseService, times(1)).findReleaseById(RELEASE_ID);
  }

    @Test
    void findReleaseByIdNotFound() throws Exception {

        // given
        when(releaseService.findReleaseById(RELEASE_ID)).thenThrow(ReleaseNotFoundException.class);

        // when/then
        mockMvc.perform(MockMvcRequestBuilders.get(RELEASE_API + "/{id}", RELEASE_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // and
        verify(releaseService, times(1)).findReleaseById(RELEASE_ID);
    }

    @Test
    void createRelease() throws Exception {

        // given
        var releaseRequest = new ReleaseRequestDTO(RELEASE_NAME, DESCRIPTION, ReleaseStatus.CREATED, RELEASE_DATE);
        ReleaseResponseDTO releaseResponseDTO = new ReleaseResponseDTO();
        releaseResponseDTO.setName(releaseRequest.getName());
        releaseResponseDTO.setDescription(releaseRequest.getDescription());
        releaseResponseDTO.setStatus(releaseRequest.getStatus());
        when(releaseService.createRelease(any())).thenReturn(releaseResponseDTO);

        // when/then
        mockMvc.perform(MockMvcRequestBuilders.post(RELEASE_API)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(releaseRequest)))
                .andExpect(status().isOk());

        // and
        verify(releaseService, times(1)).createRelease(any());
    }

    @Test
    void createReleaseBadRequest() throws Exception {

        // given
        var releaseRequest = new ReleaseRequestDTO("", DESCRIPTION, ReleaseStatus.DONE, RELEASE_DATE);

        // when/then
        mockMvc.perform(MockMvcRequestBuilders.post(RELEASE_API)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(releaseRequest)))
                .andExpect(status().isBadRequest());

        // and
        verify(releaseService, times(0)).createRelease(any());
    }

    @Test
    void updateRelease() throws Exception {

        // given
        var releaseRequest = new ReleaseRequestDTO(RELEASE_NAME, DESCRIPTION, ReleaseStatus.DONE, RELEASE_DATE);
        ReleaseResponseDTO releaseResponseDTO = new ReleaseResponseDTO();
        releaseResponseDTO.setName(releaseRequest.getName());
        releaseResponseDTO.setDescription(releaseRequest.getDescription());
        releaseResponseDTO.setStatus(releaseRequest.getStatus());
        when(releaseService.updateRelease(any(), any())).thenReturn(releaseResponseDTO);

        // when/then
        mockMvc.perform(MockMvcRequestBuilders.put(RELEASE_API + "/{id}", RELEASE_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(releaseRequest)))
                .andExpect(status().isOk());

        // and
        verify(releaseService, times(1)).updateRelease(any(), any());
    }

    @Test
    void updateReleaseNotFound() throws Exception {

        // given
        var releaseRequest = new ReleaseRequestDTO(RELEASE_NAME, DESCRIPTION, ReleaseStatus.DONE, RELEASE_DATE);
        when(releaseService.updateRelease(any(), any())).thenThrow(ReleaseNotFoundException.class);

        // when/then
        mockMvc.perform(MockMvcRequestBuilders.put(RELEASE_API + "/{id}", RELEASE_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(releaseRequest)))
                .andExpect(status().isNotFound());

        // and
        verify(releaseService, times(1)).updateRelease(any(), any());
    }

    @Test
    void updateReleaseBadRequest() throws Exception {

        // given
        var releaseRequest = new ReleaseRequestDTO("", DESCRIPTION, ReleaseStatus.DONE, RELEASE_DATE);

        // when/then
        mockMvc.perform(MockMvcRequestBuilders.put(RELEASE_API + "/{id}", RELEASE_ID)
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(releaseRequest)))
                .andExpect(status().isBadRequest());

        // and
        verify(releaseService, times(0)).updateRelease(any(), any());
    }

    @Test
    void deleteRelease() throws Exception {

        // given
        doNothing().when(releaseService).deleteRelease(RELEASE_ID);

        // when/then
        mockMvc.perform(MockMvcRequestBuilders.delete(RELEASE_API + "/{id}", RELEASE_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        // and
        verify(releaseService, times(1)).deleteRelease(RELEASE_ID);
    }

    @Test
    void deleteReleaseNotFound() throws Exception {

        // given
        doThrow(ReleaseNotFoundException.class).when(releaseService).deleteRelease(RELEASE_ID);

        // when/then
        mockMvc.perform(MockMvcRequestBuilders.delete(RELEASE_API + "/{id}", RELEASE_ID)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        // and
        verify(releaseService, times(1)).deleteRelease(RELEASE_ID);
    }

}
