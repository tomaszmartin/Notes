package pl.codeinprogress.notes.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.model.NotesRepository;
import pl.codeinprogress.notes.util.scheduler.ImmediateSchedulerProvider;
import pl.codeinprogress.notes.view.views.DetailsView;
import rx.Observable;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DetailsPresenterTest {

    @Mock
    private DetailsView detailsView;
    @Mock
    private NotesRepository notesRepository;
    private DetailsPresenter detailsPresenter;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        detailsPresenter = new DetailsPresenter(detailsView, notesRepository,
                new ImmediateSchedulerProvider());
        when(notesRepository.getNote(any())).thenReturn(Observable.just(new Note()));
    }

    @Test
    public void shouldSaveNoteInRepository() {
        detailsPresenter.saveNote(new Note(), "");
        verify(notesRepository, times(1)).saveNote(any());
    }

    @Test
    public void shouldSaveNoteEvenIfContentsAreNull() {
        detailsPresenter.saveNote(new Note(), null);
        verify(notesRepository, times(1)).saveNote(any());
    }

    @Test
    public void shouldCallGetNoteOnRepository() {
        detailsPresenter.loadNote("test");
        verify(notesRepository, times(1)).getNote("test");
    }

    @Test
    public void shouldLoadContentsIntoView() {
        detailsPresenter.showNoteContents("test");
        String encrypted = detailsPresenter.getEncrypted("test");
        verify(detailsView, times(1)).showNoteContents(encrypted);
    }

}