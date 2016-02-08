package pl.tomaszmartin.stuff;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import butterknife.ButterKnife;

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

    private void log(String message) {
        Log.d(getTag(), message);
    }

    private void setupTracker() {
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        tracker = application.getDefaultTracker();
    }

    private void pushScreen() {
        tracker.setScreenName(this.getClass().getSimpleName());
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
        log("Setting screen name: " + this.getClass().getSimpleName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
