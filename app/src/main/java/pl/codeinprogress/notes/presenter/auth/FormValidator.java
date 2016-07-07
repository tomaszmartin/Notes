package pl.codeinprogress.notes.presenter.auth;

import java.util.regex.Pattern;

/**
 * Created by tomaszmartin on 21.06.2016.
 */

public class FormValidator {

    public static boolean validateEmail(String target) {
        Pattern emailPattern = Pattern.compile(
                "[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" +
                        "\\@" +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                        "(" +
                        "\\." +
                        "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                        ")+"
        );
        return emailPattern.matcher(target).matches();
    }

    public static boolean validatePassword(String target) {
        return target.length() > 7;
    }

}
