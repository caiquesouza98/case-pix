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
class ValidatorEmailTest {

    private final ValidatorEmail validator = new ValidatorEmail();

    @Test
    void validEmailShouldReturnTrue() {
        ContaPixCreateDTO dto = mock(ContaPixCreateDTO.class);
        when(dto.getChave()).thenReturn("test@example.com");
        assertTrue(validator.validate(dto));
    }

    @Test
    void invalidEmailShouldReturnFalse() {
        ContaPixCreateDTO dto = mock(ContaPixCreateDTO.class);
        when(dto.getChave()).thenReturn("not-an-email");
        assertFalse(validator.validate(dto));
    }

    @Test
    void nullEmailShouldReturnFalse() {
        ContaPixCreateDTO dto = mock(ContaPixCreateDTO.class);
        when(dto.getChave()).thenReturn(null);
        assertFalse(validator.validate(dto));
    }
}