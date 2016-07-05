package pl.codeinprogress.notes.ui.presenters;

import android.content.Intent;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.data.firebase.FirebaseActivity;
import pl.codeinprogress.notes.data.firebase.FirebaseLink;
import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.ui.DetailsActivity;
import pl.codeinprogress.notes.ui.views.MainView;

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
        database = activity.getDatabase().getReference(FirebaseLink.forNotes());
        storage = activity.getStorage().getReferenceFromUrl(activity.getString(R.string.firebase_storage_bucket));
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
        Intent openNote = new Intent(activity, DetailsActivity.class);
        openNote.putExtra(DetailsActivity.NOTE_ID, noteId);
        activity.startActivity(openNote);
    }

}
