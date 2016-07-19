package pl.codeinprogress.notes.presenter;

import java.util.ArrayList;

import pl.codeinprogress.notes.model.Note;

/**
 * Created by tomaszmartin on 19.07.2016.
 */

public interface NotesView {

    void showNote(Note note);
    void showNotes(ArrayList<Note> notes);
    void noteDeleted();
    void noteSaved();

}
