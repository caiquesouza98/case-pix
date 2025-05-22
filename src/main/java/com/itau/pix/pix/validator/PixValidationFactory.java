package com.itau.pix.pix.validator;

import com.itau.pix.pix.exception.ValidationException;
import com.itau.pix.pix.model.enumerator.TipoChaveEnum;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class PixValidationFactory {
  private final Map<TipoChaveEnum, PixValidator> validators;

  @Autowired
  public PixValidationFactory(List<PixValidator> validatorsList) {
    validators = validatorsList.stream().collect(Collectors.toMap(
            validator -> {
              if(validator instanceof ValidatorCPF) return TipoChaveEnum.CPF;
              if(validator instanceof ValidatorCNPJ) return TipoChaveEnum.CNPJ;
              if(validator instanceof ValidatorEmail) return TipoChaveEnum.EMAIL;
              if(validator instanceof ValidatorCelular) return TipoChaveEnum.CELULAR;
              if(validator instanceof ValidatorAleatoria) return TipoChaveEnum.ALEATORIA;
              throw new ValidationException("Tipo chave invalido");
            }, validator -> validator
    ));
  }

  public PixValidator getValidator(TipoChaveEnum tipoChave) {
    return validators.get(tipoChave);
  }
}
