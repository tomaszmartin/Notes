package pl.codeinprogress.notes.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;

/**
 * Created by tomaszmartin on 12.06.16.
 *
 * Class hor handling authorization.
 */

public class AuthHandler {

    private final String ID_KEY = "USER_ID";
    private final String FIRST_NAME_KEY = "USER_FIRST_NAME";
    private final String LAST_NAME_KEY = "USE_LAST_NAME";
    private final String EMAIL_KEY = "USER_EMAIL";
    private final String STATUS = "USER_STATUS";
    private SharedPreferences manager;

    public AuthHandler(Context context) {
        manager = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public void onLogin(Credentials credentials) {
        saveCredentials(credentials);
    }

    public void onLogout() {
        Credentials credentials = new Credentials(
                null,
                null,
                null,
                null,
                false
        );
        saveCredentials(credentials);
    }

    public boolean isLogged() {
        return manager.getBoolean(STATUS, false);
    }

    private void saveCredentials(Credentials credentials) {
        Editor editor = manager.edit();
        editor.putString(ID_KEY, credentials.getId());
        editor.putString(FIRST_NAME_KEY, credentials.getFirstName());
        editor.putString(LAST_NAME_KEY, credentials.getLastName());
        editor.putString(EMAIL_KEY, credentials.getEmail());
        editor.putBoolean(STATUS, credentials.isLogged());
        editor.apply();
    }

}
