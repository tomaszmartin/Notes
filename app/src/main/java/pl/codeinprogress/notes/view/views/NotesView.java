package pl.codeinprogress.notes.view.views;

import java.util.ArrayList;

import pl.codeinprogress.notes.model.Note;

public interface NotesView {

    void showNotes(ArrayList<Note> notes);

}
