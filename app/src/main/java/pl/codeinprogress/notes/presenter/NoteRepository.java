package pl.codeinprogress.notes.presenter;

import java.util.ArrayList;

import pl.codeinprogress.notes.model.Note;

/**
 * Created by tomaszmartin on 18.07.2016.
 */

public interface NoteRepository {

    Note getNote(String noteId);
    ArrayList<Note> getNotes();
    void updateNote(Note note);
    void deleteNote(Note note);
    void addNote();

}
