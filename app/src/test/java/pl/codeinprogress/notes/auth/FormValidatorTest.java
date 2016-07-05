package pl.codeinprogress.notes.auth;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by tomaszmartin on 22.06.2016.
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
