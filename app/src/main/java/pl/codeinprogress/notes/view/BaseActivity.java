package pl.codeinprogress.notes.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import io.realm.Realm;
import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.presenter.Analytics;
import pl.codeinprogress.notes.presenter.NotesAnalytics;
import pl.codeinprogress.notes.presenter.auth.Auth;

public class BaseActivity extends AppCompatActivity {

    private Analytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Realm.init(this);
        analytics = NotesAnalytics.getInstance(this.getApplicationContext());
        analytics.sendScreen(getTag());
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
