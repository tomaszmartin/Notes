package pl.tomaszmartin.stuff;

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
    private String TAG = AnalyticsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupTracker();
        pushScreen();
    }

    private void setupTracker() {
        AnalyticsApplication application = (AnalyticsApplication) getApplication();
        tracker = application.getDefaultTracker();
    }

    private void pushScreen() {
        Log.i(TAG, "Setting screen name: " + this.getClass().getSimpleName());
        tracker.setScreenName(this.getClass().getSimpleName());
        tracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

}
