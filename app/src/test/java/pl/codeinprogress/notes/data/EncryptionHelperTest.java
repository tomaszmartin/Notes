package pl.codeinprogress.notes.data;

import android.util.Base64;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by tomaszmartin on 22.06.2016.
 */

public class EncryptionHelperTest {

    @Test
    public void helperExists() throws Exception {
        EncryptionHelper encryptionHelper = new EncryptionHelper("password");
        assertNotNull(encryptionHelper);
    }

    @Test
    public void passwordSavedCorrectly() throws Exception {
        String password = "password";
        EncryptionHelper encryptionHelper = new EncryptionHelper(password);
        assertEquals(encryptionHelper.getPassword(), password);
    }

    @Test
    public void keyExists() throws Exception {
        String password = "password";
        EncryptionHelper encryptionHelper = new EncryptionHelper(password);
        assertNotNull(encryptionHelper.getKey());
    }

    @Test
    public void cipherExists() throws Exception {
        String password = "password";
        EncryptionHelper encryptionHelper = new EncryptionHelper(password);
        assertNotNull(encryptionHelper.getCipher());
    }

    @Test
    public void messageEncryptedCorrectly() throws Exception {
        EncryptionHelper encryptionHelper = new EncryptionHelper("password");
        String message = "Test message";
        assertNotNull(encryptionHelper.encrypt(message));
    }

    @Test
    public void processWorks() throws Exception {
        EncryptionHelper encryptionHelper = new EncryptionHelper("password");
        String message = "Test message";
        String encrypted = encryptionHelper.encrypt(message);
        String decrypted = encryptionHelper.decrypt(encrypted);
        assertEquals(message, decrypted);
    }

}