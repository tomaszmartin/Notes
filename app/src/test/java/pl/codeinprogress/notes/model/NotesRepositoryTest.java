package pl.codeinprogress.notes.model;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pl.codeinprogress.notes.model.local.LocalNotesDataSource;

import static org.junit.Assert.*;

public class NotesRepositoryTest {

    @Mock
    private LocalNotesDataSource dataSource;
    private NotesRepository repository;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        repository = NotesRepository.getInstance(dataSource);
    }

    @Test
    public void shouldBeSingleton() throws Exception {
        NotesRepository anotherRepository = NotesRepository.getInstance(dataSource);
        assertEquals(repository, anotherRepository);
    }

    @Test
    public void getNotes() throws Exception {

    }

    @Test
    public void getNote() throws Exception {

    }

    @Test
    public void saveNote() throws Exception {

    }

    @Test
    public void deleteNote() throws Exception {

    }

    @Test
    public void addNote() throws Exception {

    }

}