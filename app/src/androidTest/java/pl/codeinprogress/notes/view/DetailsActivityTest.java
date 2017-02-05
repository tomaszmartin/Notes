package pl.codeinprogress.notes.view;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.matcher.ViewMatchers.*;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class DetailsActivityTest {

    @Rule
    public ActivityTestRule testRule = new ActivityTestRule<>(DetailsActivity.class);

    @Test
    public void addText() {

    }

}