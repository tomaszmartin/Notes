package pl.codeinprogress.notes.ui;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;
import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by tomaszmartin on 02.06.2016.
 */

public class BasicApplication extends Application {

    private FirebaseAnalytics analytics;

    @Override
    public void onCreate() {
        super.onCreate();
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
        analytics = FirebaseAnalytics.getInstance(this);
    }

}
