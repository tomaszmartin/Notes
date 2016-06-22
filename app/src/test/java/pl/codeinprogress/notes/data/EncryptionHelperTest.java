package pl.codeinprogress.notes.data;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by tomaszmartin on 22.06.2016.
 */

public class EncryptionHelperTest {

    @Test
    public void testShortMessage() throws Exception {
        EncryptionHelper encryptionHelper = new EncryptionHelper("password");
        String message = "Test message";
        String encrypted = encryptionHelper.encrypt(message);
        String decrypted = encryptionHelper.decrypt(encrypted);

        assertEquals(message, decrypted);
    }

    @Test
    public void testLongMessage() throws Exception {
        EncryptionHelper encryptionHelper = new EncryptionHelper("password");
        String message = "This test message is longer than the one before. \n And has multiple \n lines.";
        String encrypted = encryptionHelper.encrypt(message);
        String decrypted = encryptionHelper.decrypt(encrypted);

        assertEquals(message, decrypted);
    }

}