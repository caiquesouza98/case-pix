package com.itau.pix.pix.validator;

import com.itau.pix.pix.dto.ContaPixCreateDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ValidatorCelularTest {

    private final ValidatorCelular validator = new ValidatorCelular();

    @Test
    void validCelularShouldReturnTrue() {
        ContaPixCreateDTO dto = mock(ContaPixCreateDTO.class);
        when(dto.getChave()).thenReturn("+55 (11) 91234-5678");
        assertTrue(validator.validate(dto));
        when(dto.getChave()).thenReturn("11912345678");
        assertTrue(validator.validate(dto));
    }

    @Test
    void invalidCelularShouldReturnFalse() {
        ContaPixCreateDTO dto = mock(ContaPixCreateDTO.class);
        when(dto.getChave()).thenReturn("not_a_phone");
        assertFalse(validator.validate(dto));
    }

    @Test
    void nullCelularShouldReturnFalse() {
        ContaPixCreateDTO dto = mock(ContaPixCreateDTO.class);
        when(dto.getChave()).thenReturn(null);
        assertFalse(validator.validate(dto));
    }
}