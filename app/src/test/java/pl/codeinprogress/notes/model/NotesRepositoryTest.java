package pl.codeinprogress.notes.model;

import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;

import rx.Observable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class NotesRepositoryTest {

    private static NotesDataSource dataSource;
    private static NotesRepository repository;

    // Has to be executed once for all tests.
    // Otherwise dataSource changes but in notesRepository it is always the first instance.
    @BeforeClass
    public static void setup() {
        dataSource = Mockito.mock(NotesDataSource.class);
        repository = NotesRepository.getInstance(dataSource);
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
        Mockito.when(dataSource.getNotes()).thenReturn(Observable.empty());
        assertNotNull(repository.getNotes());
    }

}