package com.itau.pix.pix.validator;

import com.itau.pix.pix.dto.ContaPixCreateDTO;
import org.springframework.stereotype.Component;

@Component
public class ValidatorCNPJ implements PixValidator {

  @Override
  public boolean validate(ContaPixCreateDTO pix) {
    String valorChave = pix.getChave();
    return ValidatorUtils.isValidCNPJ(valorChave);
  }
}
