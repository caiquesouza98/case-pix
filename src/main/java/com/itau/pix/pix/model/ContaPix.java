package com.itau.pix.pix.model;

import com.itau.pix.pix.model.enumerator.TipoContaEnum;
import com.itau.pix.pix.model.enumerator.TipoPessoaEnum;
import com.itau.pix.pix.model.enumerator.TipoChaveEnum;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "CONTA_PIX")
public class ContaPix {

  @Version
  private Long version;

  @Id
  @GeneratedValue(strategy = GenerationType.UUID)
  private UUID id = null;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false)
  private TipoPessoaEnum tipoPessoa;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 10)
  private TipoContaEnum tipoConta;

  @Column(nullable = false, length = 4)
  private String numAgencia;

  @Column(nullable = false, length = 8)
  private String numConta;

  @Column(nullable = false, length = 30)
  private String nome;

  @Column(length = 45)
  private String sobrenome;

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 9)
  private TipoChaveEnum tipoChave;

  @Column(nullable = false, length = 77, unique = true)
  private String chave;

  @Column(nullable = false)
  private LocalDateTime dataInclusao;

  @Column
  private LocalDateTime dataInativacao;

  @Column(nullable = false)
  private boolean isAtiva;
}
