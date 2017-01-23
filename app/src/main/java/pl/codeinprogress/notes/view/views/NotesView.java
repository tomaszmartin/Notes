package pl.codeinprogress.notes.view.views;

import java.util.ArrayList;
import java.util.List;

import pl.codeinprogress.notes.model.Note;

public interface NotesView {

    void showNotes(List<Note> notes);
    void showLoadingError();
    void showLoadingIndicator();
    void hideLoadingIndicator();

}
