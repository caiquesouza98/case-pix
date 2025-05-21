package com.itau.pix.pix.service;

import com.itau.pix.pix.repository.ContaPixRepository;
import com.itau.pix.pix.model.ContaPix;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
public class ContaPixService {

  private final ContaPixRepository contaPixRepository;

  @Autowired
  public ContaPixService(ContaPixRepository accountRepository) {
    this.contaPixRepository = accountRepository;
  }

  public ContaPix save(ContaPix accountModel) {
    return contaPixRepository.save(accountModel);
  }

  public Optional<ContaPix> findById(UUID id) {
    return contaPixRepository.findById(id);
  }

  public Page<ContaPix> findAll(Pageable pageable) {
    return contaPixRepository.findAll(pageable);
  }

  public void delete(UUID id) {
    contaPixRepository.deleteById(id);
  }

}
