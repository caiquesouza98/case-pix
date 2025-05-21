package com.itau.pix.pix.enumerator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoPix {
  TELEFONE("TELEFONE"), EMAIL("EMAIL"), CPF("CPF"), CNPJ("CNPJ"), ALEATORIA("ALEATORIA");

  private final String tipo;
}
