package pl.codeinprogress.notes;

import android.test.ActivityInstrumentationTestCase2;

import pl.codeinprogress.notes.ui.MainActivity;


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