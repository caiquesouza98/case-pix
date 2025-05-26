package com.itau.pix.pix.mocks;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itau.pix.pix.dto.ContaPixCreateDTO;
import com.itau.pix.pix.dto.ContaPixUpdateDTO;
import com.itau.pix.pix.model.ContaPix;
import com.itau.pix.pix.model.enumerator.TipoChaveEnum;
import com.itau.pix.pix.model.enumerator.TipoContaEnum;
import com.itau.pix.pix.model.enumerator.TipoPessoaEnum;

import java.time.LocalDateTime;
import java.util.UUID;

public class Mocks {
  public static ContaPixCreateDTO createContaPixCreateDTO() {
    ContaPixCreateDTO dto = new ContaPixCreateDTO();
    dto.setId(UUID.randomUUID());
    dto.setNumAgencia("1111");
    dto.setNumConta("11111111");
    dto.setNome("Test");
    dto.setSobrenome("Test");
    dto.setTipoPessoa(TipoPessoaEnum.FISICA);
    dto.setTipoConta(TipoContaEnum.CORRENTE);
    dto.setTipoChave(TipoChaveEnum.CELULAR);
    dto.setChave("+5519999999999");
    return dto;
  }

  public static ContaPix createContaPix() {
    ContaPix contaPix = new ContaPix();
    contaPix.setId(UUID.randomUUID());
    contaPix.setNumAgencia("1111");
    contaPix.setNumConta("11111111");
    contaPix.setNome("Test");
    contaPix.setSobrenome("Test");
    contaPix.setTipoPessoa(TipoPessoaEnum.FISICA);
    contaPix.setTipoConta(TipoContaEnum.CORRENTE);
    contaPix.setTipoChave(TipoChaveEnum.CELULAR);
    contaPix.setChave("+5519999999999");
    contaPix.setDataInclusao(LocalDateTime.now());
    contaPix.setAtiva(Boolean.TRUE);
    return contaPix;
  }

  public static ContaPixUpdateDTO createContaPixUpdateDTO() {
    ContaPixUpdateDTO dto = new ContaPixUpdateDTO();
    dto.setId(UUID.randomUUID());
    dto.setTipoChave(TipoChaveEnum.CELULAR);
    dto.setChave("+5519999999999");
    dto.setTipoPessoa(TipoPessoaEnum.FISICA);
    dto.setTipoConta(TipoContaEnum.POUPANCA);
    dto.setNumAgencia("2222");
    dto.setNumConta("22222222");
    dto.setNome("Test");
    dto.setSobrenome("Test");
    return dto;
  }

  public static ContaPixUpdateDTO createContaPixUpdateDTONoID() {
    ContaPixUpdateDTO dto = new ContaPixUpdateDTO();
    dto.setTipoChave(TipoChaveEnum.CELULAR);
    dto.setChave("+5519999999999");
    dto.setTipoPessoa(TipoPessoaEnum.FISICA);
    dto.setTipoConta(TipoContaEnum.POUPANCA);
    dto.setNumAgencia("2222");
    dto.setNumConta("22222222");
    dto.setNome("Test");
    dto.setSobrenome("Test");
    return dto;
  }

  public static String toJson(final Object obj) {
    try {
      return new ObjectMapper().writeValueAsString(obj);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }
}
