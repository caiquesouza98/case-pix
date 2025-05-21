package com.itau.pix.pix.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ContaPix {
  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private UUID id;
  @Column(nullable = false)
  private String pessoa;
  @Column(nullable = false, length = 10)
  private String tipoConta;
  @Column(nullable = false, length = 4)
  private String numAgencia;
  @Column(nullable = false, length = 8)
  private String numConta;
  @Column(nullable = false, length = 30)
  private String nome;
  @Column(length = 45)
  private String sobrenome;
  @OneToMany(mappedBy = "conta", cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
  List<ChavePix> chavesPix;
}
