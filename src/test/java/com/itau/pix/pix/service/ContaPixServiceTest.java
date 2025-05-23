package com.itau.pix.pix.service;

import com.itau.pix.pix.dto.ContaPixCreateDTO;
import com.itau.pix.pix.dto.ContaPixUpdateDTO;
import com.itau.pix.pix.model.ContaPix;
import com.itau.pix.pix.model.enumerator.TipoChaveEnum;
import com.itau.pix.pix.repository.ContaPixRepository;
import com.itau.pix.pix.validator.PixValidationStrategy;
import com.itau.pix.pix.exception.NotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.*;

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

    Page<ContaPix> result = contaPixService.findByCriteria(
            Optional.of(id), Optional.empty(), Optional.empty(), Optional.empty(),
            Optional.empty(), Optional.empty(), Optional.empty(), Pageable.unpaged());

    assertFalse(result.getContent().isEmpty());
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
}