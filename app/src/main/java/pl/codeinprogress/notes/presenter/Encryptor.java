package pl.codeinprogress.notes.presenter;

import com.google.common.io.BaseEncoding;

import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.concurrent.Exchanger;

import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by tomaszmartin on 22.06.2016.
 */

public class Encryptor {
    private String encryption;
    private Cipher cipher;
    private Key key;
    private String password;

    public Encryptor(String pass) {
        password = pass;
        setEncryption("AES");
    }

    public String encrypt(String content) {
        try {
            if (cipher != null && key != null) {
                cipher.init(Cipher.ENCRYPT_MODE, key);
                byte[] encrypted = cipher.doFinal(content.getBytes("UTF-8"));
                return BaseEncoding.base64().encode(encrypted);
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    public String decrypt(String content) {
        try {
            if (cipher != null && key != null) {
                cipher.init(Cipher.DECRYPT_MODE, key);
                byte[] decrypted = cipher.doFinal(BaseEncoding.base64().decode(content));
                return new String(decrypted, "UTF-8");
            } else {
                return "";
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "";
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

    private void setEncryption(String encryption) {
        try {
            this.encryption = encryption;
            this.cipher = createCipher();
            this.key = createKey();
        } catch (Exception e) {
            this.encryption = encryption;
            this.cipher = null;
            this.key = null;
        }
    }

}
