package pl.codeinprogress.notes.ui;

import android.app.Application;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

/**
 * Created by tomaszmartin on 02.07.15.
 */

public class BasicActivity extends AppCompatActivity {

    private static boolean isNightMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    String getTag() {
        return this.getClass().getSimpleName();
    }

    void log(String message) {
        Log.d(getTag(), message);
    }

    void switchNightMode() {
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
        if (getApplication() instanceof BasicApplication) {
            return ((BasicApplication) getApplication()).getStorage();
        }
        return null;
    }

    public FirebaseDatabase getDatabase() {
        if (getApplication() instanceof BasicApplication) {
            return ((BasicApplication) getApplication()).getDatabase();
        }
        return null;    }

    public FirebaseAuth getAuth() {
        if (getApplication() instanceof BasicApplication) {
            return ((BasicApplication) getApplication()).getAuth();
        }
        return null;    }

    public FirebaseAnalytics getAnalytics() {
        if (getApplication() instanceof BasicApplication) {
            return ((BasicApplication) getApplication()).getAnalytics();
        }
        return null;    }

}
