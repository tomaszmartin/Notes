package pl.codeinprogress.notes.presenter;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.model.NotesRepository;
import pl.codeinprogress.notes.util.ImmediateSchedulerProvider;
import pl.codeinprogress.notes.view.views.DetailsView;

import static org.junit.Assert.*;
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
    }

    @Test
    public void shouldSaveNote() {
        detailsPresenter.saveNote(new Note(), "");
        verify(notesRepository, times(1)).saveNote(any());
    }

}