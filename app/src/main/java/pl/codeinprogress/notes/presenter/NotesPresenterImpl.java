package pl.codeinprogress.notes.presenter;

import java.util.ArrayList;

import pl.codeinprogress.notes.model.Note;

/**
 * Temporary class, should be replaced with MainPresenter and DetailsPresenter.
 */

public class NotesPresenterImpl implements NotesPresenter {

    private NotesView view;
    private NotesRepository repository;


    public NotesPresenterImpl(NotesView view) {
        this.view = view;
        this.repository = new FirebaseNotesRepository(null, this);
    }

    @Override
    public void getNotes() {
        repository.getNotes();
    }

    @Override
    public void getNote(String noteId) {
        repository.getNote(noteId);
    }

    @Override
    public void noteLoaded(Note note) {
        view.showNote(note);
    }

    @Override
    public void notesLoaded(ArrayList<Note> notes) {
        view.showNotes(notes);
    }

    @Override
    public void addNote() {
        repository.addNote();
    }

    @Override
    public void saveNote(Note note) {
        repository.saveNote(note);
    }

    @Override
    public void deleteNote(Note note) {
        repository.deleteNote(note);
    }
}
