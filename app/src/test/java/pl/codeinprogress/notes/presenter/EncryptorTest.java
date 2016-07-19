package pl.codeinprogress.notes.presenter;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by tomaszmartin on 09.07.2016.
 */

public class EncryptorTest {

    @Test
    public void savesPassword() throws Exception {
        String password = "password";
        Encryptor encryptor = new Encryptor(password);
        assertEquals(password, encryptor.getPassword());
    }

    @Test
    public void encryptsMessage() throws Exception {
        Encryptor encryptor = new Encryptor("password");
        String message = "message";

        String encrypted = encryptor.encrypt(message);
        String dectypted = encryptor.decrypt(encrypted);

        assertEquals(dectypted, message);
    }

}