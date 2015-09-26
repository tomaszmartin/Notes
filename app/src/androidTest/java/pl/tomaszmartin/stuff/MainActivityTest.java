package pl.tomaszmartin.stuff;

import android.test.ActivityInstrumentationTestCase2;


/**
 * Created by tomaszmartin on 14.09.2015.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2 {

    MainActivity activity;
    MainFragment fragment;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = (MainActivity) getActivity();
        fragment = (MainFragment) activity.getFragment();
    }

    public void testCreation() {
        assertNotNull(activity);
        assertNotNull(fragment);
    }

}