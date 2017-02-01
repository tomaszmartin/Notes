package pl.codeinprogress.notes.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;

import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.model.NotesRepository;
import pl.codeinprogress.notes.util.AndroidSchedulerProvider;
import pl.codeinprogress.notes.util.ImmediateSchedulerProvider;
import pl.codeinprogress.notes.util.SchedulerProvider;
import pl.codeinprogress.notes.view.views.NotesView;
import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NotesPresenterTest {

    @Mock
    private NotesRepository notesRepository;
    @Mock
    private NotesView notesView;
    private SchedulerProvider schedulerProvider;
    private NotesPresenter notesPresenter;

    @Before
    public void setup() {
        schedulerProvider = new ImmediateSchedulerProvider();
        MockitoAnnotations.initMocks(this);
        ArrayList<Note> notes = new ArrayList<>();
        notes.add(new Note());
        when(notesRepository.getNotes()).thenReturn(Observable.just(notes));
    }

    @Test
    public void loadNotesShouldCallShowNotes() {
        notesPresenter = new NotesPresenter(notesView, notesRepository, schedulerProvider);
        notesPresenter.loadNotes();

        verify(notesView, times(1)).showNotes(any());
    }

    @Test
    public void deleteNoteWithEmptyStringShouldNotReturnError() {
        notesPresenter.deleteNote("");
    }

    @Test
    public void deleteNoteShouldDeleteItFromRepository() {

    }

    @Test
    public void addNoteShouldAddItToRepository() {

    }

    @Test
    public void addNoteShouldOpenEditView() {

    }

}