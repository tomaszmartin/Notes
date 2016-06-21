package pl.codeinprogress.notes.auth;

import android.util.Patterns;

/**
 * Created by tomaszmartin on 21.06.2016.
 */

public class FormValidator {

    public static boolean validateEmail(String target) {
        return Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    public static boolean validatePassword(String target) {
        return target.length() > 7;
    }

}
