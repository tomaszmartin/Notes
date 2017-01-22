package pl.codeinprogress.notes.presenter;

import java.util.ArrayList;

import pl.codeinprogress.notes.model.Note;

/**
 * Created by tomaszmartin on 18.07.2016.
 */

public interface NotesRepository {

    Note get(String noteId);
    ArrayList<Note> getAll();
    void save(Note note);
    void delete(Note note);
    String add();
    ArrayList<Note> query(String query);

}
