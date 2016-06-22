package pl.codeinprogress.notes.data;

import android.util.Base64;
import android.util.Log;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by tomaszmartin on 22.06.2016.
 */

public class EncryptionHelper {
    private String encryption = "AES";
    private Cipher cipher;
    private Key key;
    private String password;

    public EncryptionHelper(String pass) {
       try {
           password = pass;
           cipher = createCipher();
           key = createKey();
       } catch (Exception e) {
           password = null;
           cipher = null;
           key = null;
       }
    }

    public String encrypt(String content) throws Exception {
        if (cipher != null && key != null) {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(content.getBytes("UTF-8"));
            return Base64.encodeToString(encrypted, Base64.DEFAULT);
        } else {
            return "";
        }
    }

    public String decrypt(String content) throws Exception {
        if (cipher != null && key != null) {
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypted = cipher.doFinal(Base64.decode(content, Base64.DEFAULT));
            return new String(decrypted, "UTF-8");
        } else {
            return "";
        }
    }

    private SecretKeySpec createKey() throws NoSuchAlgorithmException {
        MessageDigest sha = MessageDigest.getInstance("SHA-1");
        byte[] temp = sha.digest(password.getBytes());
        temp = Arrays.copyOf(temp, 16);
        return new SecretKeySpec(temp, encryption);
    }

    private Cipher createCipher() throws NoSuchAlgorithmException, NoSuchPaddingException {
        return Cipher.getInstance(encryption);
    }

    public String getPassword() {
        return this.password;
    }

    public Cipher getCipher() {
        return cipher;
    }

    public Key getKey() {
        return key;
    }

}
