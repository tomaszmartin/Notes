package pl.tomaszmartin.stuff;

import android.test.ActivityInstrumentationTestCase2;

import pl.tomaszmartin.stuff.ui.MainActivity;


/**
 * Created by tomaszmartin on 14.09.2015.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2 {

    MainActivity activity;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = (MainActivity) getActivity();
    }

    public void testCreation() {
        assertNotNull(activity);
    }

}