package com.itau.pix.pix.validator;

import com.itau.pix.pix.dto.ContaPixCreateDTO;
import com.itau.pix.pix.dto.ContaPixUpdateDTO;
import com.itau.pix.pix.exception.NotFoundException;
import com.itau.pix.pix.exception.ValidationException;
import com.itau.pix.pix.model.ContaPix;
import com.itau.pix.pix.model.enumerator.TipoContaEnum;
import com.itau.pix.pix.model.enumerator.TipoPessoaEnum;
import com.itau.pix.pix.repository.ContaPixRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PixValidationStrategy {
  private final PixValidationFactory pixValidationFactory;
  private final ContaPixRepository contaPixRepository;

  @Autowired
  public PixValidationStrategy(PixValidationFactory factory, ContaPixRepository repository) {
    this.pixValidationFactory = factory;
    this.contaPixRepository = repository;
  }

  public void requestValidator(ContaPixCreateDTO contaPixCreateDTO) {
    PixValidator validator = pixValidationFactory.getValidator(contaPixCreateDTO.getTipoChave());
    boolean isValid = validator.validate(contaPixCreateDTO);
    if (!isValid) { throw new ValidationException("Chave invalida!"); }
    limitValidator(contaPixCreateDTO);
    duplicateValidator(contaPixCreateDTO);
    requiredFieldsValidator(contaPixCreateDTO);
  }

  public void updateValidator(ContaPix contaPix, ContaPixUpdateDTO contaPixUpdateDTO) {
    shouldUpdateFieldsValidator(contaPix, contaPixUpdateDTO);
    isActive(contaPix);
    optionalFieldsValidator(contaPixUpdateDTO);
  }

  public void isActive(ContaPix contaPix) {
    if (!contaPix.isAtiva()) {
      throw new ValidationException("Esta chave está desativada!");
    }
  }

  public List<ContaPix> filterInvativa(List<ContaPix> contaPixList) {
    return contaPixList.stream().filter(contaPix -> contaPix.isAtiva()).toList();
  }

  private void requiredFieldsValidator(ContaPixCreateDTO contaPixCreateDTO) {
    if(contaPixCreateDTO.getTipoConta() == null ||
        contaPixCreateDTO.getNumAgencia() == null ||
        contaPixCreateDTO.getNumConta() == null ||
        contaPixCreateDTO.getNome() == null ||
        contaPixCreateDTO.getTipoChave() == null ||
        contaPixCreateDTO.getChave() == null) {
      throw new ValidationException("Há campos que devem ser preenchidos!");
    }
  }

  private void shouldUpdateFieldsValidator(ContaPix contaPix, ContaPixUpdateDTO contaPixUpdateDTO) {
    if(contaPixUpdateDTO.getId() != null && !contaPix.getId().equals(contaPixUpdateDTO.getId())) {
      throw new ValidationException("O ID nao deve ser alterado!");
    }

    if(contaPixUpdateDTO.getTipoChave() != null && !contaPix.getTipoChave().equals(contaPixUpdateDTO.getTipoChave())) {
      throw new ValidationException("Tipo de chave nao deve ser alterado!");
    }

    if(contaPixUpdateDTO.getChave() != null && !contaPix.getChave().equals(contaPixUpdateDTO.getChave())) {
      throw new ValidationException("Chave nao deve ser alterado!");
    }
  }

  private void optionalFieldsValidator(ContaPixUpdateDTO contaPixUpdateDTO) {
    if(contaPixUpdateDTO.getTipoConta() != null && (!contaPixUpdateDTO.getTipoConta().name().toLowerCase().matches("^(corrente|poupanca)$"))) {
      throw new ValidationException("Tipo de conta inválido!");
    }

    if(contaPixUpdateDTO.getNumAgencia() != null && !contaPixUpdateDTO.getNumAgencia().matches("\\d{4}")) {
      throw new ValidationException("Número da agência inválido! Deve ter 4 digitos!");
    }

    if(contaPixUpdateDTO.getNumConta() != null && !contaPixUpdateDTO.getNumConta().matches("\\d{8}")) {
      throw new ValidationException("Número da conta inválido! Deve ter 8 digitos!");
    }

    if(contaPixUpdateDTO.getNome() != null && (contaPixUpdateDTO.getNome().trim().isEmpty() || contaPixUpdateDTO.getNome().trim().length() > 30)) {
      throw new ValidationException("Nome inválido! Deve ter até 30 caracteres!");
    }

    if(contaPixUpdateDTO.getSobrenome() != null && (contaPixUpdateDTO.getSobrenome().trim().isEmpty() || contaPixUpdateDTO.getSobrenome().trim().length() > 45)) {
      throw new ValidationException("Sobrenome inválido! Deve ter até 45 caracteres!");
    }
  }

  private void duplicateValidator(ContaPixCreateDTO contaPixCreateDTO) {
    if(contaPixRepository.existsByChave(contaPixCreateDTO.getChave())) {
      throw new ValidationException("Valor de chave duplicado.");
    }

    if(contaPixCreateDTO.getTipoPessoa() ==  TipoPessoaEnum.FISICA) {
      if(contaPixRepository.existsByTipoChaveAndNumConta(
              contaPixCreateDTO.getTipoChave(),
              contaPixCreateDTO.getNumAgencia()
      )) {
        throw new ValidationException("Tipo de chave duplicado para essa conta.");
      }

      if(contaPixRepository.existsByChaveAndNome(
              contaPixCreateDTO.getChave(),
              contaPixCreateDTO.getNome()
      )) {
        throw new ValidationException("Chave duplicada para essa conta.");
      }
    }
  }

  private void limitValidator(ContaPixCreateDTO contaPixCreateDTO) {
    long qtdChaves = contaPixRepository.countByNumAgenciaAndNumConta(
            contaPixCreateDTO.getNumAgencia(),
            contaPixCreateDTO.getNumConta()
    );

    if(contaPixCreateDTO.getTipoPessoa() == TipoPessoaEnum.FISICA && qtdChaves >= 5) {
      throw new ValidationException("Limite de 5 chaves atingido para contas de pessoa fisica!");
    } else if (contaPixCreateDTO.getTipoPessoa() == TipoPessoaEnum.JURIDICA && qtdChaves >= 20) {
      throw new ValidationException("Limite de 20 chaves atingido para contas de pessoa juridica!");
    }
  }
}
