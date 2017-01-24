package pl.codeinprogress.notes.util;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

/**
 * Class for managing data sent to Google Analytics and Firebase Analytics.
 */

public class Analytics {

    private static Analytics instance;
    private FirebaseAnalytics firebaseAnalytics;

    private Analytics(Context context) {
        this.firebaseAnalytics = FirebaseAnalytics.getInstance(context);
    }

    /**
     * Creates and instance of @Analytics object
     *
     * @param context Android context
     * @return instance of @Analytics object
     */
    public static Analytics getInstance(Context context) {
        if (instance == null) {
            instance = new Analytics(context);
        }

        return instance;
    }

    public void sendEvent(String category, String action, String label, long value) {
        if (firebaseAnalytics != null) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, label);
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, action);
            bundle.putString(FirebaseAnalytics.Param.ITEM_CATEGORY, category);
            bundle.putDouble(FirebaseAnalytics.Param.VALUE, value);
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.VIEW_ITEM, bundle);
        }
    }

    public void sendScreen(String screenName) {
        if (firebaseAnalytics != null) {
            Log.d(screenName, "sendScreen: " + screenName);
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, screenName);
            bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, screenName);
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "screen");
            firebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
        }
    }

    public void setUserID(String userId) {
        firebaseAnalytics.setUserProperty("userID", userId);
    }

}