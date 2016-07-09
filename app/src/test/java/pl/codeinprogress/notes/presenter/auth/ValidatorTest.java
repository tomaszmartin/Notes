package pl.codeinprogress.notes.presenter.auth;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by tomaszmartin on 09.07.2016.
 */

public class ValidatorTest {

    @Test
    public void emailFails() throws Exception {
        String[] emails = {"abc", "abc@xyz", "@xyz.com"};
        for (String email: emails) {
            assertFalse(Validator.validateEmail(email));
        }
    }

    @Test
    public void emailPasses() throws Exception {
        String email = "abc@xyz.com";
        assertFalse(Validator.validateEmail(email));
    }

    @Test
    public void passwordIsTooShort() throws Exception {
        String password = "12345";
        assertFalse(Validator.validatePassword(password));
    }

    @Test
    public void passwordIsCorrect() throws Exception {
        String password = "tomek1987";
        assertTrue(Validator.validatePassword(password));
    }

}