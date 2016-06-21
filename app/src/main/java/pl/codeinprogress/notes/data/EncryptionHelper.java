package pl.codeinprogress.notes.data;

import android.util.Base64;
import android.util.Log;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.MessageDigest;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by tomaszmartin on 22.06.2016.
 */
public class EncryptionHelper {
    private String encryption = "AES";
    private Cipher cipher;
    private Key key;

    public EncryptionHelper(String password) {
        try {
            cipher = Cipher.getInstance(encryption);
            MessageDigest sha = MessageDigest.getInstance("SHA-1");
            byte[] temp = sha.digest(password.getBytes());
            temp = Arrays.copyOf(temp, 16);
            key = new SecretKeySpec(temp, encryption);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String encrypt(String content) throws Exception {
        if (cipher != null && key != null) {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            byte[] encrypted = cipher.doFinal(content.getBytes());
            String result = new String(encrypted);
            log(result);
            return result;
        } else {
            throw new InvalidKeyException("Invalid key");
        }
    }

    public String decrypt(String content) throws Exception {
        if (cipher != null && key != null) {
            byte[] ivByte = new byte[cipher.getBlockSize()];
            IvParameterSpec ivParamsSpec = new IvParameterSpec(ivByte);
            cipher.init(Cipher.DECRYPT_MODE, key, ivParamsSpec);
            byte[] decrypted = cipher.doFinal(content.getBytes());
            String result = new String(decrypted);
            log(result);
            return result;
        } else {
            throw new InvalidKeyException("Invalid key");
        }
    }

    private void log(String message) {
        Log.d(EncryptionHelper.class.getSimpleName(), message);
    }

}
