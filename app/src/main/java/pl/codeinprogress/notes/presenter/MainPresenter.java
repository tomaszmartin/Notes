package pl.codeinprogress.notes.presenter;

import android.content.Intent;
import android.os.Environment;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.util.ArrayList;

import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.view.BaseActivity;
import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.secret.Secrets;
import pl.codeinprogress.notes.view.DetailsActivity;
import pl.codeinprogress.notes.view.views.MainView;
import pl.codeinprogress.notes.view.adapters.NotesAdapter;

public class MainPresenter {

    private MainView view;
    private BaseActivity activity;
    private FirebaseDatabase database;
    private StorageReference storage;
    private NotesRepository repository;

    public MainPresenter(MainView view, BaseActivity activity) {
        this.view = view;
        this.activity = activity;
        this.repository = new RealmNotesRepository();
        this.database = FirebaseDatabase.getInstance();
        this.storage = FirebaseStorage.getInstance().getReferenceFromUrl(Secrets.FIREBASE_STORAGE);
    }

    public void addNote() {
        String noteId = repository.addNote();
        openNote(noteId);
    }

    public void openNote(Note note) {
        String noteId = note.getId();
        openNote(noteId);
    }

    public void openNote(String noteId) {
        Intent openNote = new Intent(activity, DetailsActivity.class);
        openNote.putExtra(DetailsActivity.NOTE_ID, noteId);
        activity.startActivity(openNote);
    }

    public void loadNotes() {
        ArrayList<Note> notes = repository.getNotes();
        view.showNotes(notes);
    }

    public void sortByTitle() {
        // todo: add sorting

        ArrayList<Note> notes = repository.getNotes();
        view.showNotes(notes);
    }

    public void sortByDate() {
        // todo: add sorting

        ArrayList<Note> notes = repository.getNotes();
        view.showNotes(notes);
    }

    public void search(String query) {
        ArrayList<Note> notes = repository.searchNotes(query);
        view.showNotes(notes);
    }

    public void deleteNote(Note note) {
        deleteFromFile(note);
        deleteFromRepository(note);
    }

    private void deleteFromRepository(Note note) {
        repository.deleteNote(note);
    }

    private void deleteFromFile(final Note note) {
        Runnable task = () -> {
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + note.getFileName());
            file.delete();
        };

        Thread thread = new Thread(task);
        thread.run();
    }

}
