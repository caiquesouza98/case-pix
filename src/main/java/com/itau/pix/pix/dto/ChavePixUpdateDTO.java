package com.itau.pix.pix.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChavePixUpdateDTO {

  private UUID id;

  @NotBlank
  @Pattern(regexp = "corrente|poupanca", message = "Tipo de conta inválido")
  private String tipoConta;
  @NotBlank
  @Pattern(regexp = "\\d{1,4}", message = "Número da agência inválido")
  private String numeroAgencia;
  @NotBlank
  @Pattern(regexp = "\\d{1,8}", message = "Número da conta inválido")
  private String numeroConta;
  @NotBlank
  @Size(max = 30)
  private String nomeCorrentista;
  @Size(max = 45)
  private String sobrenomeCorrentista;
}
