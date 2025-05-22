package com.itau.pix.pix.model.enumerator;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TipoChaveEnum {
  CELULAR("CELULAR"), EMAIL("EMAIL"), CPF("CPF"), CNPJ("CNPJ"), ALEATORIA("ALEATORIA");

  private final String tipo;
}
