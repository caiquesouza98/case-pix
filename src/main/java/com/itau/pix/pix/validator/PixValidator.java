package com.itau.pix.pix.validator;

import com.itau.pix.pix.dto.ContaPixCreateDTO;

public interface PixValidator {
  boolean validate(ContaPixCreateDTO pix);
}
