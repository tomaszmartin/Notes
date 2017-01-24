package pl.codeinprogress.notes.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

import io.realm.Realm;
import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.util.Analytics;

public class BaseActivity extends AppCompatActivity {

    private Analytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = Analytics.getInstance(getApplicationContext());
        analytics.sendScreen(getTag());
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
            setDay();
        } else {
            setNight();
        }
    }





    private void setDay() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        recreate();
    }

    private void setNight() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        recreate();
    }

}
