package com.itau.pix.pix.model.enumerator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoPessoaEnum {
  FISICA("FISICA"), JURIDICA("JURIDICA");

  private final String tipo;
}
