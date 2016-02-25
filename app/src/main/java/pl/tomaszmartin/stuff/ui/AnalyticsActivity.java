package pl.tomaszmartin.stuff.ui;

import android.app.UiModeManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import pl.tomaszmartin.stuff.R;

/**
 * Created by tomaszmartin on 02.07.15.
 */

public class AnalyticsActivity extends AppCompatActivity {

    private Tracker tracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupTracker();
        pushScreen();
    }

    protected String getTag() {
        return this.getClass().getSimpleName();
    }

    private void setupTracker() {
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        tracker = application.getDefaultTracker();
    }

    private void pushScreen() {
        tracker.setScreenName(this.getClass().getSimpleName());
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}
