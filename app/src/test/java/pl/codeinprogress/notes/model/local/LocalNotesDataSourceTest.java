package pl.codeinprogress.notes.model.local;

import android.content.Context;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pl.codeinprogress.notes.util.scheduler.SchedulerProvider;

import static org.junit.Assert.*;


public class LocalNotesDataSourceTest {

    @Mock
    private Context context;
    @Mock
    private SchedulerProvider schedulerProvider;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void shouldBeSingleton() {
        LocalNotesDataSource dataSource = LocalNotesDataSource.getInstance(context, schedulerProvider);
        LocalNotesDataSource shouldBeSame = LocalNotesDataSource.getInstance(context, schedulerProvider);

        assertEquals(dataSource, shouldBeSame);
    }

}