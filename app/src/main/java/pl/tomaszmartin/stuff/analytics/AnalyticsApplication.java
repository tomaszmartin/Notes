package pl.tomaszmartin.stuff.analytics;

import android.app.Application;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

import pl.tomaszmartin.stuff.R;

/**
 * Created by tomaszmartin on 02.07.15.
 */

public class AnalyticsApplication extends Application {
    private Tracker tracker;

    synchronized public Tracker getDefaultTracker() {
        if (tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            tracker = analytics.newTracker(R.xml.global_tracker);
            analytics.enableAutoActivityReports(this);
        }

        return tracker;
    }
}