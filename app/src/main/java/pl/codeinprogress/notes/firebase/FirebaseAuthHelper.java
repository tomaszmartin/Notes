package pl.codeinprogress.notes.firebase;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import pl.codeinprogress.notes.auth.Credentials;

/**
 * Created by tomaszmartin on 12.06.16.
 * Class hor handling authorization.
 */

public class FirebaseAuthHelper {

    private final String ID_KEY = "USER_ID";
    private final String NAME_KEY = "USER_NAME";
    private final String EMAIL_KEY = "USER_EMAIL";
    private final String IMAGE_KEY = "USER_IMAGE";
    private final String STATUS_KEY = "USER_STATUS";
    private SharedPreferences manager;
    private final FirebaseApplication application;
    private static FirebaseAuthHelper instance;

    private FirebaseAuthHelper(FirebaseApplication app) {
        this.application = app;
        manager = PreferenceManager.getDefaultSharedPreferences(application);
        application.getAuth().addAuthStateListener(new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = application.getAuth().getCurrentUser();
                if (user != null) {
                    Credentials credentials = Credentials.fromFirebaseUser(user);
                    onLoggedIn(credentials);
                }
            }
        });
    }

    public static FirebaseAuthHelper getInstance(FirebaseApplication app) {
        if (instance == null) {
            instance = new FirebaseAuthHelper(app);
        }

        return instance;
    }

    public void login(String email, String password, final FirebaseActivity activity) {
        application.getAuth().signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                activity.finish();
            }
        });
    }

    public void singup(final Credentials credentials, String password, final FirebaseActivity activity) {
        application.getAuth().createUserWithEmailAndPassword(credentials.getEmail(), password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        log("User has been created with email " + credentials.getEmail());
                        onSignedUp(credentials, activity);
                    }
                });
    }

    public void onSignedUp(Credentials credentials, FirebaseActivity activity) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        Uri image = null;
        try {
            image = Uri.parse(credentials.getImage());
        } catch (Exception e) {

        }


        UserProfileChangeRequest profileUpdates;
        profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(credentials.getName())
                .setPhotoUri(image)
                .build();

        if (user != null) {
            user.updateProfile(profileUpdates)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                log("Firebase user updated");
                            }
                        }
                    });
        }

        saveCredentials(credentials);
        activity.setResult(Activity.RESULT_OK);
        activity.finish();
    }

    public void onLoggedIn(Credentials credentials) {
        saveCredentials(credentials);
        log(getCredentials().toString());
    }

    public void onLoggedOut() {
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
        return manager.getBoolean(STATUS_KEY, false);
    }

    private void saveCredentials(Credentials credentials) {
        Editor editor = manager.edit();
        editor.putString(ID_KEY, credentials.getId());
        editor.putString(NAME_KEY, credentials.getName());
        editor.putString(EMAIL_KEY, credentials.getEmail());
        editor.putString(IMAGE_KEY, credentials.getImage());
        editor.putBoolean(STATUS_KEY, credentials.isLogged());
        editor.apply();
    }

    private void log(String message) {
        Log.d(FirebaseAuthHelper.class.getSimpleName(), message);
    }

    public Credentials getCredentials() {
        String name = manager.getString(NAME_KEY, null);
        String id = manager.getString(ID_KEY, null);
        String email = manager.getString(EMAIL_KEY, null);
        String image = manager.getString(IMAGE_KEY, null);
        boolean state = manager.getBoolean(STATUS_KEY, false);

        return new Credentials(name, id, email, image, state);
    }

}
