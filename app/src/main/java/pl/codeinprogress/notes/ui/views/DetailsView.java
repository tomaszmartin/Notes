package pl.codeinprogress.notes.ui.views;

import pl.codeinprogress.notes.model.Note;

/**
 * Created by tomaszmartin on 05.07.2016.
 */

public interface DetailsView {

    void viewNote(Note note);
    void viewNoteContent(String contents);

}
