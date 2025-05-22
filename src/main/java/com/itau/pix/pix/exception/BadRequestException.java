package com.itau.pix.pix.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class BadRequestException extends ResponseStatusException {
  public BadRequestException(String e) {
    super(HttpStatus.BAD_REQUEST, e);
  }
}
