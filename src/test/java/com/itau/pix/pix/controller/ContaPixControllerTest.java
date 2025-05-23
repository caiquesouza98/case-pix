package com.itau.pix.pix.controller;

import com.itau.pix.pix.dto.ContaPixCreateDTO;
import com.itau.pix.pix.dto.ContaPixUpdateDTO;
import com.itau.pix.pix.mocks.Mocks;
import com.itau.pix.pix.model.ContaPix;
import com.itau.pix.pix.service.ContaPixService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
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

    mockMvc.perform(get("/find"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  public void shouldCreatePix() throws Exception {
    ContaPixCreateDTO contaPixCreateDTO = Mocks.createContaPixCreateDTO();
    ContaPix contaPix = Mocks.createContaPix();

    when(contaPixService.save(any())).thenReturn(contaPix);

    mockMvc.perform(post("/add")
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

    mockMvc.perform(put("/update/{id}", id)
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

    mockMvc.perform(delete("/disable/{id}", id))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(contaPix.getId().toString()));
  }
}