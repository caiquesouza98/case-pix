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
class ValidatorAleatoriaTest {

    private final ValidatorAleatoria validator = new ValidatorAleatoria();

    @Test
    void validAleatoriaShouldReturnTrue() {
        ContaPixCreateDTO dto = mock(ContaPixCreateDTO.class);
        when(dto.getChave()).thenReturn("abcdefghijklmnopqrstuvwxyz1234567890");
        assertTrue(validator.validate(dto));
    }

    @Test
    void invalidAleatoriaShouldReturnFalse() {
        ContaPixCreateDTO dto = mock(ContaPixCreateDTO.class);
        when(dto.getChave()).thenReturn("short");
        assertFalse(validator.validate(dto));
    }

    @Test
    void nullAleatoriaShouldReturnFalse() {
        ContaPixCreateDTO dto = mock(ContaPixCreateDTO.class);
        when(dto.getChave()).thenReturn(null);
        assertFalse(validator.validate(dto));
    }
}