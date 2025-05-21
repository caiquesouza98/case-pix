package com.itau.pix.pix.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChavePixCreateDTO {
  @NotBlank
  @Pattern(regexp = "celular|email|cpf|cnpj|aleatorio", message = "Tipo de chave inválido")
  private String tipoChave;
  @NotBlank
  @Size(max = 77)
  private String valorChave;
  @NotBlank
  @Pattern(regexp = "FISICA|JURIDICA", message = "Tipo de pessoa deve ser FISICA ou JURIDICA")
  private String tipoPessoa;
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
