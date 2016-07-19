package pl.codeinprogress.notes.presenter;

import java.util.ArrayList;

import pl.codeinprogress.notes.model.Note;

/**
 * Created by tomaszmartin on 19.07.2016.
 */

public interface NotesPresenter {

    Note getNote(String noteId);
    ArrayList<Note> getNotes();
    void addNote();
    void saveNote(Note note);

}
