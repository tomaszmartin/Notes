package pl.codeinprogress.notes.presenter.auth;

import java.util.regex.Pattern;

public class Validator {

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
        Pattern passwordPattern = Pattern.compile(
                "[0-9a-zA-Z&@!#+]{6,20}"
        );
        return passwordPattern.matcher(target).matches();
    }

}
