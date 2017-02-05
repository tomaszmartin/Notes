package pl.codeinprogress.notes.util;

import android.content.Context;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

public class AnalyticsTest {

    @Mock
    private Context context;

    @Test
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

}