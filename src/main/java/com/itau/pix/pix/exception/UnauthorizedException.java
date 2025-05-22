package com.itau.pix.pix.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class UnauthorizedException extends ResponseStatusException {
  public UnauthorizedException(String e) {
    super(HttpStatus.UNAUTHORIZED, e);
  }
}
