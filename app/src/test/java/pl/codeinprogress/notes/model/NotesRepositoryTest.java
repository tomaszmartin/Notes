package pl.codeinprogress.notes.model;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import rx.Observable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NotesRepositoryTest {

    private static NotesDataSource dataSource;
    private static NotesRepository repository;

    // Has to be executed once for all tests.
    // Otherwise dataSource changes but in notesRepository it is always the first instance.
    @BeforeClass
    public static void setup() {
        dataSource = Mockito.mock(NotesDataSource.class);
        repository = NotesRepository.getInstance(dataSource);
        when(dataSource.getNotes()).thenReturn(Observable.empty());
    }

    @Test
    public void shouldContainDataSource() throws Exception {
        assertEquals(dataSource, repository.getDataSource());
    }

    @Test
    public void shouldBeSingleton() throws Exception {
        NotesRepository anotherRepository = NotesRepository.getInstance(dataSource);
        assertEquals(repository, anotherRepository);
    }

    @Test
    public void shouldReturnNullIfNull() throws Exception {
        assertNull(repository.getNote(null));
    }

    @Test
    public void shouldReturnNullIfEmpty() throws Exception {
        assertNull(repository.getNote(""));
    }

    @Test
    public void shouldNotReturnNull() throws Exception {
        assertNotNull(repository.getNotes());
    }

    @Test
    public void shouldCallDataSourceWhenGetHasId() throws Exception {
        repository.getNote("test");
        verify(dataSource, times(1)).getNote("test");
    }

    @Test
    public void shouldCallDataSourceWhenSave() throws Exception {
        Note tested = new Note();
        repository.saveNote(tested);
        verify(dataSource, times(1)).saveNote(tested);
    }

    @Test
    public void shouldCallDataSourceWhenDelete() throws Exception {
        String tested = "test";
        repository.deleteNote(tested);
        verify(dataSource, times(1)).deleteNote(tested);
    }

    @Test
    public void shouldCallDataSourceWhenAdd() throws Exception {
        String tested = "test";
        repository.addNote(tested);
        verify(dataSource, times(1)).addNote(tested);
    }

}