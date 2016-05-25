package pl.tomaszmartin.stuff.analytics;

import android.support.v4.app.Fragment;

/**
 * Created by tomaszmartin on 25.05.16.
 */
public class AnalyticsFragment extends Fragment {

    protected void sendAnalyticsEvent(String category, String action) {
        if (getActivity() instanceof AnalyticsActivity) {
            ((AnalyticsActivity) getActivity()).sendAnalyticsEvent(category, action);
        }
    }

}
