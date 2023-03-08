package com.assignment.releasetracker.exception;

public class ReleaseNotFoundException extends RuntimeException {
  public ReleaseNotFoundException(String message) {
    super(message);
  }
}
