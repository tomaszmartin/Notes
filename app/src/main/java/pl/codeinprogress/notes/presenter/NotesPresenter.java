package pl.codeinprogress.notes.presenter;

import java.util.ArrayList;

import pl.codeinprogress.notes.model.Note;

/**
 * Interface for presenting notes.
 */

public interface NotesPresenter {

    void getNotes();
    void getNote(String noteId);
    void noteLoaded(Note note);
    void notesLoaded(ArrayList<Note> notes);

    void addNote();
    void saveNote(Note note);
    void deleteNote(Note note);

}
