package pl.codeinprogress.notes.presenter;

import java.util.ArrayList;

import pl.codeinprogress.notes.model.Note;

/**
 * Temporary class, should be replaced with MainPresenter and DetailsPresenter.
 * // todo: delete this class
 */

public class NotesPresenterImpl implements NotesPresenter {

    private NotesView view;
    private NotesRepository repository;


    public NotesPresenterImpl(NotesView view) {
        this.view = view;
        this.repository = new RealmNotesRepository();
    }

    @Override
    public void getNotes() {
        repository.getAll();
    }

    @Override
    public void getNote(String noteId) {
        repository.get(noteId);
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
        repository.add();
    }

    @Override
    public void saveNote(Note note) {
        repository.save(note);
    }

    @Override
    public void deleteNote(Note note) {
        repository.delete(note);
    }

}
