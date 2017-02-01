package pl.codeinprogress.notes.util;

import org.junit.Test;

import pl.codeinprogress.notes.util.Encryption;

import static org.junit.Assert.*;

public class EncryptionTest {

    @Test
    public void shouldSavePassword() throws Exception {
        String password = "password";
        Encryption encryption = new Encryption(password);
        assertEquals(password, encryption.getPassword());
    }

    @Test
    public void shouldEncryptsMessage() throws Exception {
        Encryption encryption = new Encryption("password");
        String message = "message";

        String encrypted = encryption.encrypt(message);
        String decrypted = encryption.decrypt(encrypted);

        assertEquals(decrypted, message);
    }

}