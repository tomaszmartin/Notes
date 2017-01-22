package pl.codeinprogress.notes.presenter;

import android.content.Intent;
import android.os.Environment;
import android.support.annotation.NonNull;

import java.io.File;
import java.util.ArrayList;

import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.view.BaseActivity;
import pl.codeinprogress.notes.view.DetailsActivity;
import pl.codeinprogress.notes.view.views.MainView;

public class MainPresenter {

    private MainView view;
    private BaseActivity activity;
    private NotesRepository repository;

    public MainPresenter(MainView view, BaseActivity activity) {
        this.view = view;
        this.activity = activity;
        this.repository = new RealmNotesRepository();
    }

    public void addNote() {
        String noteId = repository.add();
        openNote(noteId);
    }

    public void openNote(@NonNull Note note) {
        String noteId = note.getId();
        openNote(noteId);
    }

    public void openNote(@NonNull String noteId) {
        Intent openNote = new Intent(activity, DetailsActivity.class);
        openNote.putExtra(DetailsActivity.NOTE_ID, noteId);
        activity.startActivity(openNote);
    }

    public void loadNotes() {
        ArrayList<Note> notes = repository.getAll();
        view.showNotes(notes);
    }

    public void sortByTitle() {
        // todo: add sorting

        ArrayList<Note> notes = repository.getAll();
        view.showNotes(notes);
    }

    public void sortByDate() {
        // todo: add sorting

        ArrayList<Note> notes = repository.getAll();
        view.showNotes(notes);
    }

    public void search(@NonNull String query) {
        ArrayList<Note> notes = repository.query(query);
        view.showNotes(notes);
    }

    public void deleteNote(Note note) {
        deleteFromFile(note);
        deleteFromRepository(note);
    }

    private void deleteFromRepository(@NonNull Note note) {
        repository.delete(note);
    }

    private void deleteFromFile(@NonNull Note note) {
        Runnable task = () -> {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + note.getFileName());
            file.delete();
        };

        Thread thread = new Thread(task);
        thread.run();
    }

}
