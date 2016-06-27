package pl.codeinprogress.notes.data;

import pl.codeinprogress.notes.model.Note;

/**
 * Created by tomaszmartin on 27.06.2016.
 */

public interface NoteView {

    void viewNote(Note note);
    void viewNoteContent(String contents);

}
