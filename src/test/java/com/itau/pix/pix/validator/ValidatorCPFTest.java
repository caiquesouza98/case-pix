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
class ValidatorCPFTest {

    private final ValidatorCPF validator = new ValidatorCPF();

    @Test
    void validCPFShouldReturnTrue() {
        ContaPixCreateDTO dto = mock(ContaPixCreateDTO.class);
        when(dto.getChave()).thenReturn("52998224725");
        assertTrue(validator.validate(dto));
    }

    @Test
    void invalidCPFShouldReturnFalse() {
        ContaPixCreateDTO dto = mock(ContaPixCreateDTO.class);
        when(dto.getChave()).thenReturn("00000000000");
        assertFalse(validator.validate(dto));
    }

    @Test
    void nullCPFShouldReturnFalse() {
        ContaPixCreateDTO dto = mock(ContaPixCreateDTO.class);
        when(dto.getChave()).thenReturn(null);
        assertFalse(validator.validate(dto));
    }
}