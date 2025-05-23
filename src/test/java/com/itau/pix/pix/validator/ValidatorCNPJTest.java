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
class ValidatorCNPJTest {

    private final ValidatorCNPJ validator = new ValidatorCNPJ();

    @Test
    void validCNPJShouldReturnTrue() {
        ContaPixCreateDTO dto = mock(ContaPixCreateDTO.class);
        when(dto.getChave()).thenReturn("32861205000158");
        assertTrue(validator.validate(dto));
    }

    @Test
    void invalidCNPJShouldReturnFalse() {
        ContaPixCreateDTO dto = mock(ContaPixCreateDTO.class);
        when(dto.getChave()).thenReturn("00000000000000");
        assertFalse(validator.validate(dto));
    }

    @Test
    void nullCNPJShouldReturnFalse() {
        ContaPixCreateDTO dto = mock(ContaPixCreateDTO.class);
        when(dto.getChave()).thenReturn(null);
        assertFalse(validator.validate(dto));
    }
}