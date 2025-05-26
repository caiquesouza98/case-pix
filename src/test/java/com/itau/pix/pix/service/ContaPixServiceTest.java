package com.itau.pix.pix.service;

import com.itau.pix.pix.dto.ContaPixCreateDTO;
import com.itau.pix.pix.dto.ContaPixUpdateDTO;
import com.itau.pix.pix.exception.NotFoundException;
import com.itau.pix.pix.model.ContaPix;
import com.itau.pix.pix.model.enumerator.TipoChaveEnum;
import com.itau.pix.pix.repository.ContaPixRepository;
import com.itau.pix.pix.validator.PixValidationStrategy;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ContaPixServiceTest {

  @InjectMocks
  private ContaPixService contaPixService;

  @Mock
  private ContaPixRepository contaPixRepository;

  @Mock
  private PixValidationStrategy pixValidationStrategy;

  @Test
  public void shouldSaveContaPix() {
    ContaPixCreateDTO dto = mock(ContaPixCreateDTO.class);
    when(dto.getId()).thenReturn(null);

    ContaPix persisted = new ContaPix();
    when(contaPixRepository.saveAndFlush(any())).thenReturn(persisted);

    ContaPix result = contaPixService.save(dto);

    verify(pixValidationStrategy).requestValidator(dto);
    verify(contaPixRepository).saveAndFlush(any(ContaPix.class));
    assertNotNull(result);
  }

  @Test
  public void shouldUpdateContaPix() {
    UUID id = UUID.randomUUID();
    ContaPixUpdateDTO dto = mock(ContaPixUpdateDTO.class);
    ContaPix contaPix = new ContaPix();
    when(contaPixRepository.findById(id)).thenReturn(Optional.of(contaPix));
    when(contaPixRepository.save(any())).thenReturn(contaPix);

    ContaPix result = contaPixService.update(id, dto);

    verify(pixValidationStrategy).updateValidator(contaPix, dto);
    verify(contaPixRepository).save(any(ContaPix.class));
    assertNotNull(result);
  }

  @Test
  public void shouldFindContaPix() {
    UUID id = UUID.randomUUID();
    ContaPix contaPix = new ContaPix();
    when(contaPixRepository.findById(id)).thenReturn(Optional.of(contaPix));

    Optional<ContaPix> result = contaPixService.findById(id);

    assertTrue(result.isPresent());
    assertEquals(contaPix, result.get());
  }

  @Test
  public void shouldFindByCriteriaById() {
    UUID id = UUID.randomUUID();
    ContaPix contaPix = new ContaPix();
    when(contaPixRepository.findById(id)).thenReturn(Optional.of(contaPix));

    when(pixValidationStrategy.filterInvativa(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

    Page<ContaPix> result = contaPixService.findByCriteria(
            Optional.of(id), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.empty(), Pageable.unpaged());

    assertFalse(result.getContent().isEmpty());
  }

  @Test
  public void shouldFindByCriteriaByTipoChave() {
    TipoChaveEnum tipoChave = TipoChaveEnum.CPF;
    ContaPix contaPix = new ContaPix();
    List<ContaPix> contaPixList = List.of(contaPix);
    when(contaPixRepository.findByTipoChave(tipoChave)).thenReturn(contaPixList);

    when(pixValidationStrategy.filterInvativa(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

    Page<ContaPix> result = contaPixService.findByCriteria(
            Optional.empty(), Optional.of(tipoChave), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.empty(), Pageable.unpaged());

    assertFalse(result.getContent().isEmpty());
    assertEquals(contaPixList.size(), result.getNumberOfElements());
  }

  @Test
  public void shouldFindByCriteriaByNumAgenciaAndNumConta() {
    String numAgencia = "1234";
    String numConta = "567890";
    ContaPix contaPix = new ContaPix();
    List<ContaPix> contaPixList = List.of(contaPix);
    when(contaPixRepository.findByNumAgenciaAndNumConta(numAgencia, numConta)).thenReturn(contaPixList);

    when(pixValidationStrategy.filterInvativa(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

    Page<ContaPix> result = contaPixService.findByCriteria(
            Optional.empty(), Optional.empty(), Optional.of(numAgencia), Optional.of(numConta),
            Optional.empty(), Optional.empty(), Optional.empty(), Pageable.unpaged());

    assertFalse(result.getContent().isEmpty());
    assertEquals(contaPixList.size(), result.getNumberOfElements());
  }

  @Test
  public void shouldThrowWhenFindByCriteriaReturnsEmpty() {
    when(contaPixRepository.findByNome(any())).thenReturn(Collections.emptyList());

    assertThrows(NotFoundException.class, () ->
            contaPixService.findByCriteria(
                    Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                    Optional.of("Alice"), Optional.empty(), Optional.empty(), Pageable.unpaged()
            ));
  }


  @Test
  public void shouldDisableContaPix() {
    UUID id = UUID.randomUUID();
    ContaPix contaPix = new ContaPix();
    contaPix.setAtiva(true);

    when(contaPixRepository.findById(id)).thenReturn(Optional.of(contaPix));
    when(contaPixRepository.save(any())).thenReturn(contaPix);

    ContaPix result = contaPixService.disableById(id);

    verify(pixValidationStrategy).isActive(contaPix);
    verify(contaPixRepository).save(contaPix);
    assertFalse(result.isAtiva());
    assertNotNull(result.getDataInativacao());
  }

  @Test
  public void shouldThrowWhenNoContaPixFoundByCriteria() {
    when(contaPixRepository.findByNome(any())).thenReturn(Collections.emptyList());

    assertThrows(NotFoundException.class, () ->
            contaPixService.findByCriteria(
                    Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                    Optional.of("Alice"), Optional.empty(), Optional.empty(), Pageable.unpaged()
            ));
  }

  @Test
  public void shouldFindByCriteriaByDataInclusao() {
    LocalDateTime dataInclusao = LocalDateTime.now();
    ContaPix contaPix = new ContaPix();
    List<ContaPix> contaPixList = List.of(contaPix);
    when(contaPixRepository.findByDataInclusao(dataInclusao)).thenReturn(contaPixList);

    when(pixValidationStrategy.filterInvativa(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

    Page<ContaPix> result = contaPixService.findByCriteria(
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.of(dataInclusao), Optional.empty(), Pageable.unpaged());

    assertFalse(result.getContent().isEmpty());
    assertEquals(contaPixList.size(), result.getNumberOfElements());
  }

  @Test
  public void shouldFindByCriteriaByDataInativacao() {
    LocalDateTime dataInativacao = LocalDateTime.now();
    ContaPix contaPix = new ContaPix();
    List<ContaPix> contaPixList = List.of(contaPix);
    when(contaPixRepository.findByDataInativacao(dataInativacao)).thenReturn(contaPixList);

    when(pixValidationStrategy.filterInvativa(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

    Page<ContaPix> result = contaPixService.findByCriteria(
            Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.of(dataInativacao), Pageable.unpaged());

    assertFalse(result.getContent().isEmpty());
    assertEquals(contaPixList.size(), result.getNumberOfElements());
  }

  @Test
  public void shouldFindByCriteriaWithMultipleFilters() {
    TipoChaveEnum tipoChave = TipoChaveEnum.CELULAR;
    String numAgencia = "5678";
    String numConta = "123456";
    String nome = "Teste";
    LocalDateTime dataInclusao = LocalDateTime.now(); // Adicionando dataInclusao
    ContaPix contaPix = new ContaPix();
    List<ContaPix> contaPixList = List.of(contaPix);
    when(contaPixRepository.findByTipoChaveAndNumAgenciaAndNumContaAndNomeAndDataInclusao(any(), any(), any(), any(), any()))
            .thenReturn(contaPixList);

    when(pixValidationStrategy.filterInvativa(anyList())).thenAnswer(invocation -> invocation.getArgument(0));

    Page<ContaPix> result = contaPixService.findByCriteria(
            Optional.empty(), Optional.of(tipoChave), Optional.of(numAgencia), Optional.of(numConta),
            Optional.of(nome), Optional.of(dataInclusao), Optional.empty(), Pageable.unpaged());

    assertFalse(result.getContent().isEmpty());
    assertEquals(contaPixList.size(), result.getNumberOfElements());
  }

  @Test
  public void shouldThrowWhenFindByCriteriaByDataInclusaoReturnsEmpty() {
    LocalDateTime dataInclusao = LocalDateTime.now();
    when(contaPixRepository.findByDataInclusao(dataInclusao)).thenReturn(Collections.emptyList());

    assertThrows(NotFoundException.class, () ->
            contaPixService.findByCriteria(
                    Optional.empty(), Optional.empty(), Optional.empty(), Optional.empty(),
                    Optional.empty(), Optional.of(dataInclusao), Optional.empty(), Pageable.unpaged()
            ));
  }
}