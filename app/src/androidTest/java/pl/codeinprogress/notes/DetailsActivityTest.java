package pl.codeinprogress.notes;

import android.app.Activity;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.test.ActivityInstrumentationTestCase2;
import android.test.TouchUtils;
import android.widget.EditText;
import android.widget.ImageView;

import pl.codeinprogress.notes.ui.DetailsActivity;
import pl.codeinprogress.notes.ui.DetailsFragment;

/**
 * Created by tomaszmartin on 09.09.2015.
 */

public class DetailsActivityTest extends ActivityInstrumentationTestCase2<DetailsActivity> {

    Activity activity;
    DetailsFragment fragment;

    public DetailsActivityTest() {
        super(DetailsActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        activity = getActivity();
        fragment = (DetailsFragment) ((DetailsActivity) activity).getFragment();
    }

    public void testActivityIsNotNull() {
        assertNotNull(activity);
    }

    public void testFragmentIsNotNull() {
        assertNotNull(fragment);
    }

    public void testContentGetter() {
        final EditText contentView = (EditText) activity.findViewById(R.id.content_view);
        assertNotNull(contentView);

        // Get focus on a view
        TouchUtils.clickView(this, contentView);

        // Send text to the view
        sendKeys("testing 1, 2, 3...");
        assertEquals(fragment.getNoteContent(), contentView.getText().toString(), fragment.getNoteContent());
    }

    public void testFontSize() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        String key = activity.getString(R.string.font_size_preference);
        String val = preferences.getString(key, "100");
        preferences.edit().putString(key, "20").apply();
        assertEquals(20, fragment.getFontSize());
        preferences.edit().putString(key, val).apply();
    }

    public void testImageRemoval() {
        fragment.removeImage();
        ImageView imageView = (ImageView) activity.findViewById(R.id.image_view);
        assertNotNull(imageView);
        assertNull(imageView.getDrawable());
    }

}