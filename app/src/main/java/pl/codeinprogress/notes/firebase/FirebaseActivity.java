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

/**
 * Created by tomaszmartin on 02.07.15.
 */

public class FirebaseActivity extends AppCompatActivity {

    private static boolean isNightMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getApplication() instanceof FirebaseApplication) {
            log("Trying to load Firebase config");
            getConfiguration().fetch(500).addOnSuccessListener(new OnSuccessListener<Void>() {

                @Override
                public void onSuccess(Void aVoid) {
                    log("Succeeded to load Firebase config");
                    onFirebaseConfigFetched();
                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    log("Failed to load Firebase config");
                }
            });

        }
    }

    public String getTag() {
        return this.getClass().getSimpleName();
    }

    public void log(String message) {
        Log.d(getTag(), message);
    }

    public void switchNightMode() {
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            isNightMode = false;
            recreate();
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            isNightMode = true;
            recreate();
        }
    }

    public FirebaseStorage getStorage() {
        if (getApplication() instanceof FirebaseApplication) {
            return ((FirebaseApplication) getApplication()).getStorage();
        }
        return null;
    }

    public FirebaseDatabase getDatabase() {
        if (getApplication() instanceof FirebaseApplication) {
            return ((FirebaseApplication) getApplication()).getDatabase();
        }
        return null;    }

    public FirebaseAuth getAuth() {
        if (getApplication() instanceof FirebaseApplication) {
            return ((FirebaseApplication) getApplication()).getAuth();
        }
        return null;    }

    public FirebaseAnalytics getAnalytics() {
        if (getApplication() instanceof FirebaseApplication) {
            return ((FirebaseApplication) getApplication()).getAnalytics();
        }
        return null;
    }

    public FirebaseRemoteConfig getConfiguration() {
        if (getApplication() instanceof FirebaseApplication) {
            return ((FirebaseApplication) getApplication()).getConfiguration();
        }
        return null;
    }

    public void onFirebaseConfigFetched() {

    }

}
