package pl.tomaszmartin.stuff.ui;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by tomaszmartin on 02.07.15.
 */

public class BasicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    protected String getTag() {
        return this.getClass().getSimpleName();
    }

    protected void log(String message) {
        Log.d(getTag(), message);
    }

}
