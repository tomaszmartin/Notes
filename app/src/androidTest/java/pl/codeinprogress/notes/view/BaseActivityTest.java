package pl.codeinprogress.notes.view;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class BaseActivityTest {

    @Rule
    public ActivityTestRule activityTestRule = new ActivityTestRule<>(BaseActivity.class);

}