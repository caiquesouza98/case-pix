package com.itau.pix.pix.validator;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
class ValidatorUtilsTest {

    @Test
    void isValidEmailShouldValidate() {
        assertTrue(ValidatorUtils.isValidEmail("a@b.com"));
        assertFalse(ValidatorUtils.isValidEmail("a@b"));
        assertFalse(ValidatorUtils.isValidEmail(null));
    }

    @Test
    void isValidCelularShouldValidate() {
        assertTrue(ValidatorUtils.isValidCelular("+55 (11) 91234-5678"));
        assertTrue(ValidatorUtils.isValidCelular("11912345678"));
        assertFalse(ValidatorUtils.isValidCelular("invalid"));
        assertFalse(ValidatorUtils.isValidCelular(null));
    }

    @Test
    void isValidCPFShouldValidate() {
        assertTrue(ValidatorUtils.isValidCPF("52998224725")); // valid
        assertFalse(ValidatorUtils.isValidCPF("00000000000")); // repeated
        assertFalse(ValidatorUtils.isValidCPF("invalid"));
        assertFalse(ValidatorUtils.isValidCPF(null));
    }

    @Test
    void isValidCNPJShouldValidate() {
        assertTrue(ValidatorUtils.isValidCNPJ("32861205000158")); // valid
        assertFalse(ValidatorUtils.isValidCNPJ("00000000000000")); // repeated
        assertFalse(ValidatorUtils.isValidCNPJ("invalid"));
        assertFalse(ValidatorUtils.isValidCNPJ(null));
    }

    @Test
    void isValidAleatoriaShouldValidate() {
        assertTrue(ValidatorUtils.isValidAleatoria("abcdefghijklmnopqrstuvwxyz1234567890"));
        assertFalse(ValidatorUtils.isValidAleatoria("short"));
        assertFalse(ValidatorUtils.isValidAleatoria(null));
    }
}