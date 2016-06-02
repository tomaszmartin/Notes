package pl.codeinprogress.notes.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

/**
 * Created by tomaszmartin on 02.07.15.
 */

public class BasicActivity extends AppCompatActivity {

    private static boolean isNightMode = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    String getTag() {
        return this.getClass().getSimpleName();
    }

    void log(String message) {
        Log.d(getTag(), message);
    }

    void switchNightMode() {
        if (isNightMode) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            isNightMode = false;
            recreate();
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            isNightMode = true;
            recreate();
        }
    }

}
