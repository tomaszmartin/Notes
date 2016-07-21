package pl.codeinprogress.notes.presenter.firebase;

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

public class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!Auth.getInstance(this).isLogged()) {
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

    public void authenticate() {

    }

    public Auth getAuthHandler() {
        return Auth.getInstance(this);
    }

}
