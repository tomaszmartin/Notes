package pl.codeinprogress.notes.firebase;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.FirebaseDatabase;

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

    public FirebaseDatabase getDatabase() {
        if (getActivity() instanceof FirebaseActivity) {
            return ((FirebaseActivity) getActivity()).getDatabase();
        } else {
            return null;
        }
    }

}
