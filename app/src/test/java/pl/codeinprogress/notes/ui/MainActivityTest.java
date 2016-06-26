package pl.codeinprogress.notes.ui;

import android.os.Build;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import pl.codeinprogress.notes.BuildConfig;
import pl.codeinprogress.notes.firebase.FirebaseApplication;

/**
 * Created by tomaszmartin on 26.06.2016.
 */

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, application = FirebaseApplication.class)
public class MainActivityTest {

    @Test
    public void dummyTest() {

    }

}