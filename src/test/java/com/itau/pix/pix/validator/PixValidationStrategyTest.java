package com.itau.pix.pix.validator;

import com.itau.pix.pix.dto.ContaPixCreateDTO;
import com.itau.pix.pix.dto.ContaPixUpdateDTO;
import com.itau.pix.pix.exception.NotFoundException;
import com.itau.pix.pix.exception.ValidationException;
import com.itau.pix.pix.model.ContaPix;
import com.itau.pix.pix.model.enumerator.TipoChaveEnum;
import com.itau.pix.pix.model.enumerator.TipoContaEnum;
import com.itau.pix.pix.model.enumerator.TipoPessoaEnum;
import com.itau.pix.pix.repository.ContaPixRepository;
import com.itau.pix.pix.mocks.Mocks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class PixValidationStrategyTest {

  @Mock
  private PixValidationFactory pixValidationFactory;

  @Mock
  private ContaPixRepository contaPixRepository;

  @Mock
  private PixValidator pixValidator;

  @InjectMocks
  private PixValidationStrategy pixValidationStrategy;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.openMocks(this);
    pixValidationStrategy = new PixValidationStrategy(pixValidationFactory, contaPixRepository);
  }

  @Test
  void requestValidatorShouldThrowIfPixInvalid() {
    ContaPixCreateDTO dto = Mocks.createContaPixCreateDTO();
    when(pixValidationFactory.getValidator(any())).thenReturn(pixValidator);
    when(pixValidator.validate(dto)).thenReturn(false);

    assertThrows(ValidationException.class, () -> pixValidationStrategy.requestValidator(dto));
    verify(pixValidator).validate(dto);
  }

  @Test
  void requestValidatorShouldThrowIfRequiredFieldsMissing() {
    ContaPixCreateDTO dto = Mocks.createContaPixCreateDTO();
    when(pixValidationFactory.getValidator(any())).thenReturn(pixValidator);
    when(pixValidator.validate(dto)).thenReturn(true);

    dto.setChave(null);
    assertThrows(ValidationException.class, () -> pixValidationStrategy.requestValidator(dto));
  }

  @Test
  void requestValidatorShouldThrowOnDuplicateChave() {
    ContaPixCreateDTO dto = Mocks.createContaPixCreateDTO();
    when(pixValidationFactory.getValidator(any())).thenReturn(pixValidator);
    when(pixValidator.validate(dto)).thenReturn(true);
    when(contaPixRepository.existsByChave(anyString())).thenReturn(true);

    assertThrows(ValidationException.class, () -> pixValidationStrategy.requestValidator(dto));
  }

  @Test
  void requestValidatorShouldThrowOnFisicaLimits() {
    ContaPixCreateDTO dto = Mocks.createContaPixCreateDTO();
    dto.setTipoPessoa(TipoPessoaEnum.FISICA);
    when(pixValidationFactory.getValidator(any())).thenReturn(pixValidator);
    when(pixValidator.validate(dto)).thenReturn(true);
    when(contaPixRepository.countByNumAgenciaAndNumConta(anyString(), anyString())).thenReturn(6L);

    assertThrows(ValidationException.class, () -> pixValidationStrategy.requestValidator(dto));
  }

  @Test
  void requestValidatorShouldThrowOnJuridicaLimits() {
    ContaPixCreateDTO dto = Mocks.createContaPixCreateDTO();
    dto.setTipoPessoa(TipoPessoaEnum.JURIDICA);
    when(pixValidationFactory.getValidator(any())).thenReturn(pixValidator);
    when(pixValidator.validate(dto)).thenReturn(true);
    when(contaPixRepository.countByNumAgenciaAndNumConta(anyString(), anyString())).thenReturn(21L);

    assertThrows(ValidationException.class, () -> pixValidationStrategy.requestValidator(dto));
  }

  @Test
  void duplicateValidatorShouldThrowOnDuplicateTipoChaveFisica() {
    ContaPixCreateDTO dto = Mocks.createContaPixCreateDTO();
    dto.setTipoPessoa(TipoPessoaEnum.FISICA);
    when(contaPixRepository.existsByChave(dto.getChave())).thenReturn(false);
    when(contaPixRepository.existsByTipoChaveAndNumConta(dto.getTipoChave(), dto.getNumAgencia())).thenReturn(true);
    when(pixValidationFactory.getValidator(any())).thenReturn(pixValidator);
    when(pixValidator.validate(dto)).thenReturn(true);

    assertThrows(ValidationException.class, () -> pixValidationStrategy.requestValidator(dto));
  }

  @Test
  void duplicateValidatorShouldThrowOnDuplicateNomeFisica() {
    ContaPixCreateDTO dto = Mocks.createContaPixCreateDTO();
    dto.setTipoPessoa(TipoPessoaEnum.FISICA);
    when(contaPixRepository.existsByChave(dto.getChave())).thenReturn(false);
    when(contaPixRepository.existsByTipoChaveAndNumConta(dto.getTipoChave(), dto.getNumAgencia())).thenReturn(false);
    when(contaPixRepository.existsByChaveAndNome(dto.getChave(), dto.getNome())).thenReturn(true);
    when(pixValidationFactory.getValidator(any())).thenReturn(pixValidator);
    when(pixValidator.validate(dto)).thenReturn(true);

    assertThrows(ValidationException.class, () -> pixValidationStrategy.requestValidator(dto));
  }

  @Test
  void isActiveThrowsIfNotActive() {
    ContaPix contaPix = Mocks.createContaPix();
    contaPix.setAtiva(false);
    assertThrows(ValidationException.class, () -> pixValidationStrategy.isActive(contaPix));
  }

  @Test
  void updateValidatorShouldThrowIfIdChanged() {
    ContaPix contaPix = Mocks.createContaPix();
    ContaPixUpdateDTO updateDTO = Mocks.createContaPixUpdateDTO();
    updateDTO.setId(java.util.UUID.randomUUID());
    assertThrows(ValidationException.class, () -> pixValidationStrategy.updateValidator(contaPix, updateDTO));
  }

  @Test
  void updateValidatorShouldThrowIfTipoChaveChanged() {
    ContaPix contaPix = Mocks.createContaPix();
    ContaPixUpdateDTO updateDTO = Mocks.createContaPixUpdateDTO();
    updateDTO.setId(contaPix.getId());
    updateDTO.setTipoChave(TipoChaveEnum.EMAIL);
    assertThrows(ValidationException.class, () -> pixValidationStrategy.updateValidator(contaPix, updateDTO));
  }

  @Test
  void updateValidatorShouldThrowIfChaveChanged() {
    ContaPix contaPix = Mocks.createContaPix();
    ContaPixUpdateDTO updateDTO = Mocks.createContaPixUpdateDTO();
    updateDTO.setId(contaPix.getId());
    updateDTO.setTipoChave(contaPix.getTipoChave());
    updateDTO.setChave("teste");
    assertThrows(ValidationException.class, () -> pixValidationStrategy.updateValidator(contaPix, updateDTO));
  }

  @Test
  void updateValidatorShouldAllowIfNoChange() {
    ContaPix contaPix = Mocks.createContaPix();
    ContaPixUpdateDTO updateDTO = Mocks.createContaPixUpdateDTO();
    updateDTO.setId(contaPix.getId());
    updateDTO.setTipoChave(contaPix.getTipoChave());
    updateDTO.setChave(contaPix.getChave());
    assertDoesNotThrow(() -> pixValidationStrategy.updateValidator(contaPix, updateDTO));
  }

  @Test
  void optionalFieldsValidatorThrowsOnInvalidNumAgencia() {
    ContaPixUpdateDTO dto = Mocks.createContaPixUpdateDTO();
    dto.setNumAgencia("12");
    assertThrows(ValidationException.class, () -> {
      pixValidationStrategy.updateValidator(Mocks.createContaPix(), dto);
    });
  }

  @Test
  void optionalFieldsValidatorThrowsOnInvalidNumConta() {
    ContaPixUpdateDTO dto = Mocks.createContaPixUpdateDTO();
    dto.setNumConta("1234");
    assertThrows(ValidationException.class, () -> {
      pixValidationStrategy.updateValidator(Mocks.createContaPix(), dto);
    });
  }

  @Test
  void optionalFieldsValidatorThrowsOnInvalidNome() {
    ContaPixUpdateDTO dto = Mocks.createContaPixUpdateDTO();
    dto.setNome(" ");
    assertThrows(ValidationException.class, () -> {
      pixValidationStrategy.updateValidator(Mocks.createContaPix(), dto);
    });

    dto.setNome("a".repeat(31));
    assertThrows(ValidationException.class, () -> {
      pixValidationStrategy.updateValidator(Mocks.createContaPix(), dto);
    });
  }

  @Test
  void optionalFieldsValidatorThrowsOnInvalidSobrenome() {
    ContaPixUpdateDTO dto = Mocks.createContaPixUpdateDTO();
    dto.setSobrenome("");
    assertThrows(ValidationException.class, () -> {
      pixValidationStrategy.updateValidator(Mocks.createContaPix(), dto);
    });

    dto.setSobrenome("a".repeat(46));
    assertThrows(ValidationException.class, () -> {
      pixValidationStrategy.updateValidator(Mocks.createContaPix(), dto);
    });
  }
@Test
void optionalFieldsValidatorShouldAcceptValidTipoConta() {
  ContaPixUpdateDTO dto = Mocks.createContaPixUpdateDTONoID();
  dto.setTipoConta(TipoContaEnum.CORRENTE);

  assertDoesNotThrow(() -> {
    pixValidationStrategy.updateValidator(Mocks.createContaPix(), dto);
  });
  dto.setTipoConta(TipoContaEnum.POUPANCA);
  assertDoesNotThrow(() -> {
    pixValidationStrategy.updateValidator(Mocks.createContaPix(), dto);
  });
}

@Test
void optionalFieldsValidatorShouldAcceptValidNumAgencia() {
  ContaPixUpdateDTO dto = Mocks.createContaPixUpdateDTONoID();
  dto.setNumAgencia("1234");
  assertDoesNotThrow(() -> {
    pixValidationStrategy.updateValidator(Mocks.createContaPix(), dto);
  });
}

@Test
void optionalFieldsValidatorShouldAcceptValidNumConta() {
  ContaPixUpdateDTO dto = Mocks.createContaPixUpdateDTONoID();
  dto.setNumConta("12345678");
  assertDoesNotThrow(() -> {
    pixValidationStrategy.updateValidator(Mocks.createContaPix(), dto);
  });
}

@Test
void optionalFieldsValidatorShouldAcceptValidNome() {
  ContaPixUpdateDTO dto = Mocks.createContaPixUpdateDTONoID();
  dto.setNome("Nome Válido");
  assertDoesNotThrow(() -> {
    pixValidationStrategy.updateValidator(Mocks.createContaPix(), dto);
  });
  dto.setNome("a".repeat(30));
  assertDoesNotThrow(() -> {
    pixValidationStrategy.updateValidator(Mocks.createContaPix(), dto);
  });
}

@Test
void optionalFieldsValidatorShouldAcceptValidSobrenome() {
  ContaPixUpdateDTO dto = Mocks.createContaPixUpdateDTONoID();
  dto.setSobrenome("Sobrenome Válido");
  assertDoesNotThrow(() -> {
    pixValidationStrategy.updateValidator(Mocks.createContaPix(), dto);
  });
  dto.setSobrenome("a".repeat(45));
  assertDoesNotThrow(() -> {
    pixValidationStrategy.updateValidator(Mocks.createContaPix(), dto);
  });
}

@Test
void optionalFieldsValidatorShouldRejectNumAgenciaWithNonDigits() {
  ContaPixUpdateDTO dto = Mocks.createContaPixUpdateDTONoID();
  dto.setNumAgencia("123a");
  assertThrows(ValidationException.class, () -> {
    pixValidationStrategy.updateValidator(Mocks.createContaPix(), dto);
  });
}

@Test
void optionalFieldsValidatorShouldRejectNumContaWithNonDigits() {
  ContaPixUpdateDTO dto = Mocks.createContaPixUpdateDTO();
  dto.setNumConta("1234567a");
  
  assertThrows(ValidationException.class, () -> {
    pixValidationStrategy.updateValidator(Mocks.createContaPix(), dto);
  });
}

@Test
void optionalFieldsValidatorShouldRejectNumAgenciaWithWrongLength() {
  ContaPixUpdateDTO dto = Mocks.createContaPixUpdateDTO();
  dto.setNumAgencia("123");
  assertThrows(ValidationException.class, () -> {
    pixValidationStrategy.updateValidator(Mocks.createContaPix(), dto);
  });
  dto.setNumAgencia("12345");
  assertThrows(ValidationException.class, () -> {
    pixValidationStrategy.updateValidator(Mocks.createContaPix(), dto);
  });
}

@Test
void optionalFieldsValidatorShouldRejectNumContaWithWrongLength() {
  ContaPixUpdateDTO dto = Mocks.createContaPixUpdateDTO();
  dto.setNumConta("1234567");
  assertThrows(ValidationException.class, () -> {
    pixValidationStrategy.updateValidator(Mocks.createContaPix(), dto);
  });
  dto.setNumConta("123456789");
  assertThrows(ValidationException.class, () -> {
    pixValidationStrategy.updateValidator(Mocks.createContaPix(), dto);
  });
}

@Test
void optionalFieldsValidatorShouldAcceptWhenAllFieldsAreMissing() {
  ContaPixUpdateDTO dto = new ContaPixUpdateDTO();
  assertDoesNotThrow(() -> {
    pixValidationStrategy.updateValidator(Mocks.createContaPix(), dto);
  });
}

@Test
void optionalFieldsValidatorShouldAcceptWhenAllFieldsAreValid() {
  ContaPixUpdateDTO dto = Mocks.createContaPixUpdateDTONoID();
  dto.setTipoConta(TipoContaEnum.CORRENTE);
  dto.setNumAgencia("1234");
  dto.setNumConta("12345678");
  dto.setNome("Nome Válido");
  dto.setSobrenome("Sobrenome Válido");
  assertDoesNotThrow(() -> {
    pixValidationStrategy.updateValidator(Mocks.createContaPix(), dto);
  });
}
}