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
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class NotesPresenterTest {

    @Mock
    private NotesRepository notesRepository;
    @Mock
    private NotesView notesView;
    private NotesPresenter notesPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        notesPresenter = new NotesPresenter(notesView, notesRepository,
                new ImmediateSchedulerProvider());

        ArrayList<Note> notes = new ArrayList<>();
        notes.add(new Note());
        when(notesRepository.getNotes()).thenReturn(Observable.just(notes));
    }

    @Test
    public void loadNotesShouldCallLoadNotesOnRepository() {
        notesPresenter.loadNotes();
        verify(notesRepository, times(1)).getNotes();
    }

    @Test
    public void loadNotesShouldCallShowNotes() {
        notesPresenter.loadNotes();
        verify(notesView, times(1)).showNotes(any());
    }

    @Test
    public void deleteNoteShouldDeleteItFromRepository() {
        notesPresenter.deleteNote("");
        verify(notesRepository, times(1)).deleteNote(any());
    }

    @Test
    public void addNoteShouldAddItToRepository() {
        notesPresenter.addNote();
        verify(notesRepository, times(1)).addNote(any());
    }

    @Test
    public void addNoteShouldOpenEditView() {
        notesPresenter.addNote();
        verify(notesView, times(1)).openEditView(any());
    }

}