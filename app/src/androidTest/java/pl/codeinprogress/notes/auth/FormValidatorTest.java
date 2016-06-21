package pl.codeinprogress.notes.auth;

import org.junit.Test;

import java.text.Normalizer;

import static org.junit.Assert.*;

/**
 * Created by tomaszmartin on 21.06.2016.
 */

public class FormValidatorTest {

    @Test
    public void testValidateEmail() throws Exception {
        String emptyEmail = "";
        String noDomainEmail = "test@pl";
        String badEmail = "testtest.pl";
        String goodEmail = "test@test.pl";

        assertFalse(FormValidator.validateEmail(emptyEmail));
        assertFalse(FormValidator.validateEmail(noDomainEmail));
        assertFalse(FormValidator.validateEmail(badEmail));
        assertTrue(FormValidator.validateEmail(goodEmail));
    }

    @Test
    public void testValidatePassword() throws Exception {
        String emptyPassword = "";
        String shortPassword = "1234567";
        String goodPassword = "12345678";

        assertFalse(FormValidator.validatePassword(emptyPassword));
        assertFalse(FormValidator.validatePassword(shortPassword));
        assertTrue(FormValidator.validatePassword(goodPassword));
    }

}