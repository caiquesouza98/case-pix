package com.itau.pix.pix.validator;

import com.itau.pix.pix.dto.ContaPixCreateDTO;
import com.itau.pix.pix.exception.NotFoundException;
import com.itau.pix.pix.exception.ValidationException;
import com.itau.pix.pix.model.enumerator.TipoPessoaEnum;
import com.itau.pix.pix.repository.ContaPixRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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
  }

  private void duplicateValidator(ContaPixCreateDTO contaPixCreateDTO) {
    if(contaPixRepository.existsByValorPix(contaPixCreateDTO.getChave())) {
      throw new ValidationException("Valor de chave duplicado.");
    }

    if(contaPixCreateDTO.getTipoPessoa() ==  TipoPessoaEnum.FISICA) {
      if(contaPixRepository.existsByTipoChaveAndNumConta(
              contaPixCreateDTO.getTipoChave(),
              contaPixCreateDTO.getNumAgencia()
      )) {
        throw new ValidationException("Tipo de chave duplicado para essa conta.");
      }

      if(contaPixRepository.existsByValorPixAndNome(
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

    if(contaPixCreateDTO.getTipoPessoa() == TipoPessoaEnum.FISICA && qtdChaves > 5) {
      throw new NotFoundException("Limite de 5 chaves atingido para contas de pessoa fisica!");
    } else if (contaPixCreateDTO.getTipoPessoa() == TipoPessoaEnum.JURIDICA && qtdChaves > 20) {
      throw new NotFoundException("Limite de 20 chaves atingido para contas de pessoa juridica!");
    }
  }
}
