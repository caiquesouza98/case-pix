package com.itau.pix.pix.enumerator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoContaEnum {
  POUPANCA("POUPANCA"), CORRENTE("CORRENTE");

  private final String nome;
}
