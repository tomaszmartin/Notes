package pl.tomaszmartin.stuff.analytics;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

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

    protected void log(String message) {
        Log.d(getTag(), message);
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

    public void sendAnalyticsEvent(String category, String action) {
        ((AnalyticsApplication) getApplication()).getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory(category)
                .setAction(action)
                .setLabel(getTag())
                .build());
    }

}
