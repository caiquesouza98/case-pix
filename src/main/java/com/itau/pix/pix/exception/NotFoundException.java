package com.itau.pix.pix.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class NotFoundException extends ResponseStatusException {
  public NotFoundException(String e) {
    super(HttpStatus.NOT_FOUND, e);
  }
}
