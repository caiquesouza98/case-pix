package com.itau.pix.pix.controller;

import com.itau.pix.pix.dto.ContaPixCreateDTO;
import com.itau.pix.pix.dto.ContaPixUpdateDTO;
import com.itau.pix.pix.exception.BadRequestException;
import com.itau.pix.pix.exception.NotFoundException;
import com.itau.pix.pix.exception.UnauthorizedException;
import com.itau.pix.pix.exception.ValidationException;
import com.itau.pix.pix.mocks.Mocks;
import com.itau.pix.pix.model.ContaPix;
import com.itau.pix.pix.service.ContaPixService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ContaPixController.class)
@ActiveProfiles("test")
public class ContaPixControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private ContaPixService contaPixService;

  @Test
  public void shouldReturnPixList() throws Exception {
    Page<ContaPix> page = new PageImpl<>(List.of(Mocks.createContaPix()));
    when(contaPixService.findByCriteria(
            any(), any(), any(), any(), any(), any(), any(), any(Pageable.class)
    )).thenReturn(page);

    mockMvc.perform(get("/api/v1/"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  public void shouldCreatePix() throws Exception {
    ContaPixCreateDTO contaPixCreateDTO = Mocks.createContaPixCreateDTO();
    ContaPix contaPix = Mocks.createContaPix();

    when(contaPixService.save(any())).thenReturn(contaPix);

    mockMvc.perform(post("/api/v1/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Mocks.toJson(contaPixCreateDTO)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(contaPix.getId().toString()));
  }

  @Test
  public void shouldUpdatePix() throws Exception {
    UUID id = UUID.randomUUID();
    ContaPixUpdateDTO updateRequest = Mocks.createContaPixUpdateDTO();
    ContaPix contaPix = Mocks.createContaPix();
    contaPix.setId(id);

    when(contaPixService.update(eq(id), any(ContaPixUpdateDTO.class))).thenReturn(contaPix);

    mockMvc.perform(put("/api/v1/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Mocks.toJson(updateRequest)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(contaPix.getId().toString()));
  }

  @Test
  public void shouldDisablePix() throws Exception {
    UUID id = UUID.randomUUID();
    ContaPix contaPix = Mocks.createContaPix();
    contaPix.setId(id);

    when(contaPixService.disableById(id)).thenReturn(contaPix);

    mockMvc.perform(delete("/api/v1/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(contaPix.getId().toString()));
  }

  @Test
  public void shouldReturnUnprocessableEntityWhenSaveThrowsValidationException() throws Exception {
    String errorMessage = "Chave invalida!";
    ContaPixCreateDTO contaPixCreateDTO = Mocks.createContaPixCreateDTO();

    when(contaPixService.save(any())).thenThrow(new ValidationException(errorMessage));

    mockMvc.perform(post("/api/v1/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Mocks.toJson(contaPixCreateDTO)))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.message").value(errorMessage))
            .andExpect(jsonPath("$.status").value(401));
  }

  @Test
  public void shouldReturnUnprocessableEntityWhenUpdateThrowsValidationException() throws Exception {
    UUID id = UUID.randomUUID();
    String errorMessage = "O ID nao deve ser alterado!";
    ContaPixUpdateDTO updateRequest = Mocks.createContaPixUpdateDTO();

    when(contaPixService.update(eq(id), any(ContaPixUpdateDTO.class)))
            .thenThrow(new ValidationException(errorMessage));

    mockMvc.perform(put("/api/v1/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Mocks.toJson(updateRequest)))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.message").value(errorMessage))
            .andExpect(jsonPath("$.status").value(401));
  }

  @Test
  public void shouldReturnUnprocessableEntityWhenDisableThrowsValidationException() throws Exception {
    UUID id = UUID.randomUUID();
    String errorMessage = "Esta chave está desativada!";

    when(contaPixService.disableById(id)).thenThrow(new ValidationException(errorMessage));

    mockMvc.perform(delete("/api/v1/{id}", id))
            .andExpect(status().isUnprocessableEntity())
            .andExpect(jsonPath("$.message").value(errorMessage))
            .andExpect(jsonPath("$.status").value(401));
  }

  @Test
  public void shouldReturnNotFoundWhenFindByIdThrowsNotFoundException() throws Exception {
    UUID id = UUID.randomUUID();
    String errorMessage = "Não encontrado";

    when(contaPixService.findByCriteria(
            any(), any(), any(), any(), any(), any(), any(), any()
    )).thenThrow(new NotFoundException(errorMessage));

    mockMvc.perform(get("/api/v1/")
                    .param("id", id.toString()))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value(errorMessage))
            .andExpect(jsonPath("$.status").value(401));
  }

  @Test
  public void shouldReturnNotFoundWhenUpdateThrowsNotFoundException() throws Exception {

    UUID id = UUID.randomUUID();
    String errorMessage = "Não encontrado";
    ContaPixUpdateDTO updateRequest = Mocks.createContaPixUpdateDTO();

    when(contaPixService.update(eq(id), any(ContaPixUpdateDTO.class)))
            .thenThrow(new NotFoundException(errorMessage));

    mockMvc.perform(put("/api/v1/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Mocks.toJson(updateRequest)))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value(errorMessage))
            .andExpect(jsonPath("$.status").value(401));
  }

  @Test
  public void shouldReturnNotFoundWhenDisableThrowsNotFoundException() throws Exception {
    UUID id = UUID.randomUUID();
    String errorMessage = "Não encontrado";

    when(contaPixService.disableById(id)).thenThrow(new NotFoundException(errorMessage));

    mockMvc.perform(delete("/api/v1/{id}", id))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value(errorMessage))
            .andExpect(jsonPath("$.status").value(401));
  }

  @Test
  public void shouldReturnNotFoundWhenFindByCriteriaThrowsNotFoundException() throws Exception {
    String errorMessage = "Nenhuma chave encontrada";

    when(contaPixService.findByCriteria(
            any(), any(), any(), any(), any(), any(), any(), any()
    )).thenThrow(new NotFoundException(errorMessage));

    mockMvc.perform(get("/api/v1/"))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.message").value(errorMessage))
            .andExpect(jsonPath("$.status").value(401));
  }


  @Test
  public void shouldReturnUnauthorizedWhenFindThrowsUnauthorizedException() throws Exception {
    String errorMessage = "Usuário não autorizado";
    when(contaPixService.findByCriteria(
            any(), any(), any(), any(), any(), any(), any(), any(Pageable.class)
    )).thenThrow(new UnauthorizedException(errorMessage));

    mockMvc.perform(get("/api/v1/"))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value(errorMessage));
  }

  @Test
  public void shouldReturnUnauthorizedWhenSaveThrowsUnauthorizedException() throws Exception {
    String errorMessage = "Usuário não autorizado para criar chave PIX";
    ContaPixCreateDTO contaPixCreateDTO = Mocks.createContaPixCreateDTO();

    when(contaPixService.save(any())).thenThrow(new UnauthorizedException(errorMessage));

    mockMvc.perform(post("/api/v1/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Mocks.toJson(contaPixCreateDTO)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value(errorMessage));
  }

  @Test
  public void shouldReturnUnauthorizedWhenUpdateThrowsUnauthorizedException() throws Exception {
    UUID id = UUID.randomUUID();
    String errorMessage = "Usuário não autorizado para atualizar chave PIX";
    ContaPixUpdateDTO updateRequest = Mocks.createContaPixUpdateDTO();

    when(contaPixService.update(eq(id), any(ContaPixUpdateDTO.class)))
            .thenThrow(new UnauthorizedException(errorMessage));

    mockMvc.perform(put("/api/v1/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Mocks.toJson(updateRequest)))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value(errorMessage));
  }

  @Test
  public void shouldReturnUnauthorizedWhenDisableThrowsUnauthorizedException() throws Exception {
    UUID id = UUID.randomUUID();
    String errorMessage = "Usuário não autorizado para desativar chave PIX";

    when(contaPixService.disableById(id)).thenThrow(new UnauthorizedException(errorMessage));

    mockMvc.perform(delete("/api/v1/{id}", id))
            .andExpect(status().isUnauthorized())
            .andExpect(jsonPath("$.message").value(errorMessage));
  }

  @Test
  public void shouldReturnBadRequestWhenFindThrowsBadRequestException() throws Exception {
    String errorMessage = "Parâmetros de busca inválidos";
    when(contaPixService.findByCriteria(
            any(), any(), any(), any(), any(), any(), any(), any(Pageable.class)
    )).thenThrow(new BadRequestException(errorMessage));

    mockMvc.perform(get("/api/v1/"))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(errorMessage));
  }

  @Test
  public void shouldReturnBadRequestWhenSaveThrowsBadRequestException() throws Exception {
    String errorMessage = "Dados inválidos para criação da chave PIX";
    ContaPixCreateDTO contaPixCreateDTO = Mocks.createContaPixCreateDTO();

    when(contaPixService.save(any())).thenThrow(new BadRequestException(errorMessage));

    mockMvc.perform(post("/api/v1/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Mocks.toJson(contaPixCreateDTO)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(errorMessage));
  }

  @Test
  public void shouldReturnBadRequestWhenUpdateThrowsBadRequestException() throws Exception {
    UUID id = UUID.randomUUID();
    String errorMessage = "Dados inválidos para atualização da chave PIX";
    ContaPixUpdateDTO updateRequest = Mocks.createContaPixUpdateDTO();

    when(contaPixService.update(eq(id), any(ContaPixUpdateDTO.class)))
            .thenThrow(new BadRequestException(errorMessage));

    mockMvc.perform(put("/api/v1/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(Mocks.toJson(updateRequest)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(errorMessage));
  }

  @Test
  public void shouldReturnBadRequestWhenDisableThrowsBadRequestException() throws Exception {
    UUID id = UUID.randomUUID();
    String errorMessage = "ID inválido fornecido para desativação";

    when(contaPixService.disableById(id)).thenThrow(new BadRequestException(errorMessage));

    mockMvc.perform(delete("/api/v1/{id}", id))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message").value(errorMessage));
  }

  @Test
  public void shouldReturnBadRequestWhenInputJsonIsInvalid() throws Exception {
    String invalidJson = "{\"tipoConta\": \"INVALID_TYPE\"}";

    mockMvc.perform(post("/api/v1/")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidJson))
            .andExpect(status().isBadRequest());
  }

  @Test
  public void shouldReturnBadRequestWhenUpdateWithInvalidJson() throws Exception {
    UUID id = UUID.randomUUID();
    String invalidJson = "{\"numAgencia\": \"abcde\"}";

    mockMvc.perform(put("/api/v1/{id}", id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(invalidJson))
            .andExpect(status().isBadRequest());
  }

}