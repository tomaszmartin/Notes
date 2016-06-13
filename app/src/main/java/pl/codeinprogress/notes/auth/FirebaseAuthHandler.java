package pl.codeinprogress.notes.auth;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.PreferenceManager;
import pl.codeinprogress.notes.firebase.FirebaseApplication;

/**
 * Created by tomaszmartin on 12.06.16.
 *
 * Class hor handling authorization.
 */

public class FirebaseAuthHandler {

    private final String ID_KEY = "USER_ID";
    private final String FIRST_NAME_KEY = "USER_NAME";
    private final String LAST_NAME_KEY = "USE_LAST_NAME";
    private final String EMAIL_KEY = "USER_EMAIL";
    private final String IMAGE_KEY = "USER_IMAGE";
    private final String STATUS_KEY = "USER_STATUS";
    private SharedPreferences manager;
    private FirebaseApplication application;

    public FirebaseAuthHandler(FirebaseApplication application) {
        this.application = application;
        manager = PreferenceManager.getDefaultSharedPreferences(application);
    }

    public void login(String email, String password) {

    }

    public void singup(Credentials credentials) {

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
                null,
                false
        );
        saveCredentials(credentials);
    }

    public boolean isLogged() {
        return manager.getBoolean(STATUS_KEY, false);
    }

    private void saveCredentials(Credentials credentials) {
        Editor editor = manager.edit();
        editor.putString(ID_KEY, credentials.getId());
        editor.putString(FIRST_NAME_KEY, credentials.getFirstName());
        editor.putString(LAST_NAME_KEY, credentials.getLastName());
        editor.putString(EMAIL_KEY, credentials.getEmail());
        editor.putString(IMAGE_KEY, credentials.getImage());
        editor.putBoolean(STATUS_KEY, credentials.isLogged());
        editor.apply();
    }

}
