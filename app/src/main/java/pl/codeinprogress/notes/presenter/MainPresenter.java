package pl.codeinprogress.notes.presenter;

import android.content.Intent;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.storage.StorageReference;
import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.model.data.firebase.FirebaseActivity;
import pl.codeinprogress.notes.model.data.firebase.FirebaseLink;
import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.view.DetailsActivity;
import pl.codeinprogress.notes.presenter.views.MainView;
import pl.codeinprogress.notes.view.adapters.NotesAdapter;

/**
 * Created by tomaszmartin on 05.07.2016.
 */

public class MainPresenter {

    private MainView noteView;
    private FirebaseActivity activity;
    private DatabaseReference database;
    private StorageReference storage;

    public MainPresenter(MainView noteView, FirebaseActivity activity) {
        this.noteView = noteView;
        this.activity = activity;
        this.database = activity.getDatabase().getReference(FirebaseLink.forNotes());
        this.storage = activity.getStorage().getReferenceFromUrl(activity.getString(R.string.firebase_storage_bucket));
    }

    public void addNote() {
        DatabaseReference noteReference = database.push();
        String noteId = noteReference.getKey();
        Note note = new Note(noteId);
        noteReference.setValue(note);
        openNote(note);
    }

    public void deleteNote(Note note) {
        DatabaseReference noteReference = activity.getDatabase().getReference(FirebaseLink.forNote(note));
        noteReference.removeValue();
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
        NotesAdapter adapter = new NotesAdapter(activity, Note.class, R.layout.main_item, database);
        noteView.notesLoaded(adapter);
    }

    public void sortByTitle() {
        Query reference = activity.getDatabase().getReference(FirebaseLink.forNotes()).orderByChild("title");
        NotesAdapter adapter = new NotesAdapter(activity, Note.class, R.layout.main_item, reference);
        noteView.notesLoaded(adapter);
    }

    public void sortByDate() {
        Query reference = activity.getDatabase().getReference(FirebaseLink.forNotes()).orderByChild("lastModified");
        NotesAdapter adapter = new NotesAdapter(activity, Note.class, R.layout.main_item, reference);
        noteView.notesLoaded(adapter);
    }

    public void search(String query) {
        Query reference = activity.getDatabase().getReference(FirebaseLink.forNotes()).orderByChild("title").startAt(query);
        NotesAdapter adapter = new NotesAdapter(activity, Note.class, R.layout.main_item, reference);
        noteView.notesLoaded(adapter);
    }

}
