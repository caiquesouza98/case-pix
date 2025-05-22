package com.itau.pix.pix.repository;

import com.itau.pix.pix.model.enumerator.TipoChaveEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import com.itau.pix.pix.model.ContaPix;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface ContaPixRepository extends JpaRepository<ContaPix, UUID> {

  boolean existsByValorPix(String valorPix);
  boolean existsByTipoChaveAndNumConta(TipoChaveEnum tipoChave, String numConta);
  boolean existsByValorPixAndNome(String valorPix, String nome);
  List<ContaPix> findByNome(String nome);
  List<ContaPix> findByTipoChave(TipoChaveEnum tipoChave);
  List<ContaPix> findByDataInclusao(LocalDateTime dataInclusao);
  List<ContaPix> findByDataInativacao(LocalDateTime dataInativacao);
  List<ContaPix> findByNumAgenciaAndNumConta(String numAgencia, String numConta);
  List<ContaPix> findByTipoChaveAndNumAgenciaAndNumConta(TipoChaveEnum tipoChave, String numAgencia, String numConta);
  List<ContaPix> findByTipoChaveAndNumAgenciaAndNumContaAndNomeAndDataInclusao(TipoChaveEnum tipoChave, String numAgencia, String numConta, String nome, LocalDateTime dataInclusao);
  long countByNumAgenciaAndNumConta(String numAgencia, String numConta);
}
