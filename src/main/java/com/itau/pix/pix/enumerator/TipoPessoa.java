package com.itau.pix.pix.enumerator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoPessoa {
  FISICA("FISICA"), JURIDICA("JURIDICA");

  private final String tipo;
}
