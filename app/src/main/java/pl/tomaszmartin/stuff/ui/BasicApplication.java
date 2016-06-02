package pl.tomaszmartin.stuff.ui;

import android.app.Application;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

/**
 * Created by tomaszmartin on 02.06.2016.
 */

public class BasicApplication extends Application {

    static {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO);
    }

}
