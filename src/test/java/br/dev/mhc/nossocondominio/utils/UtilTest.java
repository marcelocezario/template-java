package br.dev.mhc.nomeaplicacao.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UtilTest {

    @Test
    public void shouldValidateIfTextIsInteger() {
        assertFalse(Util.isIntegerNumber(null));
        assertFalse(Util.isIntegerNumber(""));
        assertFalse(Util.isIntegerNumber("abc123"));
        assertFalse(Util.isIntegerNumber("123.56"));
        assertFalse(Util.isIntegerNumber("test"));
        assertFalse(Util.isIntegerNumber("test 321"));
        assertFalse(Util.isIntegerNumber("1234 56"));

        assertTrue(Util.isIntegerNumber("1234"));
        assertTrue(Util.isIntegerNumber("-1234"));
    }

    @Test
    public void shouldValidateIfTextIsNumber() {
        assertFalse(Util.isNumber(null));
        assertFalse(Util.isNumber(""));
        assertFalse(Util.isNumber("abc123"));
        assertFalse(Util.isNumber("123456 789"));

        assertTrue(Util.isNumber("1234"));
        assertTrue(Util.isNumber("1234.56"));
        assertTrue(Util.isNumber("-4321"));
        assertTrue(Util.isNumber("-999.56"));
    }

    @Test
    public void shouldValidateIfTextIsValidEmail() {
        assertFalse(Util.isValidEmail(null));
        assertFalse(Util.isValidEmail(""));
        assertFalse(Util.isValidEmail("test"));
        assertFalse(Util.isValidEmail("test.com"));
        assertFalse(Util.isValidEmail("test@test .com"));

        assertTrue(Util.isValidEmail("test@test.com"));
    }

}
