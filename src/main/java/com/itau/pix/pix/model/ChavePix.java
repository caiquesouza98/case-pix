package com.itau.pix.pix.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class ChavePix {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;
  @Column(nullable = false, length = 9)
  private String tipo;
  @Column(nullable = false, length = 77, unique = true)
  private String valor;
  @Column(nullable = false)
  private LocalDateTime dataInclusao;
  @Column(nullable = false)
  private boolean ativa;

  @ManyToOne
  @JoinColumn(name = "conta_id", nullable = false)
  private ContaPix conta;

}
