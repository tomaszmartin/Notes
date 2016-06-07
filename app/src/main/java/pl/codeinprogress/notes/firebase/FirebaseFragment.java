package pl.codeinprogress.notes.firebase;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Created by tomaszmartin on 07.06.16.
 */
public class FirebaseFragment extends Fragment {

    public FirebaseAnalytics getAnalytics() {
        if (getActivity() instanceof FirebaseActivity) {
            return ((FirebaseActivity) getActivity()).getAnalytics();
        } else {
            return null;
        }
    }

    public void logEvent(String name, Bundle params) {
        if (getAnalytics() != null) {
            getAnalytics().logEvent(name, params);
        }
    }

}
