package pl.codeinprogress.notes.firebase;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;

import pl.codeinprogress.notes.R;

/**
 * Created by tomaszmartin on 02.07.15.
 */

public class FirebaseActivity extends AppCompatActivity {

    private FirebaseAnalytics analytics;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FirebaseRemoteConfig configuration;
    private FirebaseStorage storage;
    private FirebaseAuthHelper authHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = FirebaseAnalytics.getInstance(this);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        storage = FirebaseStorage.getInstance();
        configuration = FirebaseRemoteConfig.getInstance();
        configuration.setDefaults(R.xml.firebase);
        authHandler = FirebaseAuthHelper.getInstance(this);
        fetchConfig();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!getAuthHandler().isLogged()) {
            authenticate();
        }
    }

    public String getTag() {
        return this.getClass().getSimpleName();
    }

    public void log(String message) {
        Log.d(getTag(), message);
    }

    public void switchNightMode() {
        boolean isNight = getResources().getBoolean(R.bool.isNight);
        if (isNight) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            recreate();
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            recreate();

        }
    }

    public void fetchConfig() {
        log("Fetching Firebase remote config");
        getConfiguration().fetch(500).addOnSuccessListener(new OnSuccessListener<Void>() {

            @Override
            public void onSuccess(Void aVoid) {
                getConfiguration().activateFetched();
                onConfigFetched();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });
    }

    public void onConfigFetched() {
    }

    public void logEvent(String name, Bundle params) {
        if (getAnalytics() != null) {
            getAnalytics().logEvent(name, params);
        }
    }

    public void authenticate() {

    }

    public FirebaseAnalytics getAnalytics() {
        return analytics;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public FirebaseRemoteConfig getConfiguration() {
        return configuration;
    }

    public FirebaseStorage getStorage() {
        return storage;
    }

    public FirebaseAuthHelper getAuthHandler() {
        return authHandler;
    }
}
