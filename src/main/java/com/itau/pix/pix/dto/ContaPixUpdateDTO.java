package com.itau.pix.pix.dto;

import com.itau.pix.pix.model.enumerator.TipoChaveEnum;
import com.itau.pix.pix.model.enumerator.TipoContaEnum;
import com.itau.pix.pix.model.enumerator.TipoPessoaEnum;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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
public class ContaPixUpdateDTO {

  private UUID id;
  private TipoChaveEnum tipoChave;
  private String chave;

  @NotNull(message = "O tipo de conta é obrigatório!")
  @Enumerated(EnumType.STRING)
  private TipoContaEnum tipoConta;

  @NotNull(message = "O tipo da pessoa é obrigatório!")
  @Enumerated(EnumType.STRING)
  private TipoPessoaEnum tipoPessoa;

  @NotBlank(message = "O numero da agência é obrigatório!")
  @Pattern(regexp = "\\d{1,4}", message = "Número da agência inválido! Deve ter 4 digitos!")
  private String numAgencia;

  @NotBlank(message = "O numero da conta é obrigatório!")
  @Pattern(regexp = "\\d{1,8}", message = "Número da conta inválido! Deve ter 8 digitos!")
  private String numConta;

  @NotBlank
  @Size(max = 30)
  private String nome;

  @Size(max = 45)
  private String sobrenome;
}
