package pl.codeinprogress.notes.ui;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * Created by tomaszmartin on 12.06.16.
 */

public class AuthHandler {

    private final String ID_KEY = "USER_ID";
    private final String FIRST_NAME_KEY = "USER_FIRST_NAME";
    private final String LAST_NAME_KEY = "USE_LAST_NAME";
    private final String EMAIL_KEY = "USER_EMAIL";
    private SharedPreferences manager;

    public AuthHandler(Context context) {
        manager = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void onLogin() {
        Editor editor = manager.edit();
    }

    public void onLogout() {

    }

    public boolean isLogged() {
        return false;
    }

}
