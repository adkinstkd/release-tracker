package com.assignment.releasetracker.exception;

import com.assignment.releasetracker.dto.ErrorResponseDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

  /**
   * Creates release not found error response
   *
   * @param exception {@link ReleaseNotFoundException}
   * @return {@link ResponseEntity} containing {@link ErrorResponseDTO}
   */
  @ExceptionHandler(ReleaseNotFoundException.class)
  @ResponseStatus(HttpStatus.NOT_FOUND)
  public ResponseEntity<ErrorResponseDTO> notFoundException(ReleaseNotFoundException exception) {
    return new ResponseEntity<>(new ErrorResponseDTO(exception.getMessage()), HttpStatus.NOT_FOUND);
  }

}
