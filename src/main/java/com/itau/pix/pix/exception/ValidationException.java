package com.itau.pix.pix.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class ValidationException extends ResponseStatusException {
  public ValidationException(String reason) {
    super(HttpStatus.UNPROCESSABLE_ENTITY, reason);
  }
}
