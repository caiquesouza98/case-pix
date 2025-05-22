package com.itau.pix.pix.controller;

import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.itau.pix.pix.service.ContaPixService;
import com.itau.pix.pix.model.ContaPix;
import com.itau.pix.pix.dto.ContaPixUpdateDTO;
import com.itau.pix.pix.dto.ContaPixCreateDTO;

import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin
@RequestMapping("/")
public class ContaPixController {

  private final ContaPixService contaPixService;

  @Autowired
  public ContaPixController(ContaPixService contaPixService) {
    this.contaPixService = contaPixService;
  }

  @GetMapping
  public ResponseEntity<Page<ContaPix>> getAccounts(@PageableDefault() Pageable pageable) {
    return ResponseEntity.ok(contaPixService.findAll(pageable));
  }

  @GetMapping("/{id}")
  public ResponseEntity<ContaPix> getAccount(@PathVariable UUID id) {
    Optional<ContaPix> ContaPix = contaPixService.findById(id);
    return ContaPix.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<ContaPix> saveAccount(@RequestBody @Valid ContaPixCreateDTO accountDTO) {
    ContaPix ContaPix = new ContaPix();
    BeanUtils.copyProperties(accountDTO, ContaPix);
    return ResponseEntity.status(HttpStatus.CREATED).body(contaPixService.save(ContaPix));
  }

  @PutMapping("/{id}")
  public ResponseEntity<ContaPix> updateAccount(@PathVariable UUID id, @RequestBody @Valid ContaPixUpdateDTO accountDTO) {
    Optional<ContaPix> account = contaPixService.findById(id);
    if (account.isPresent()) {
      BeanUtils.copyProperties(accountDTO, account.get(), "id");
      account.get().setId(id);
      return ResponseEntity.ok(contaPixService.save(account.get()));
    } else {
      return ResponseEntity.notFound().build();
    }
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<ContaPix> deleteAccount(@PathVariable UUID id) {
    Optional<ContaPix> account = contaPixService.findById(id);
    if (account.isPresent()) {
      contaPixService.delete(account.get().getId());
      return ResponseEntity.status(HttpStatus.OK).build();
    } else {
      return ResponseEntity.notFound().build();
    }
  }
}
