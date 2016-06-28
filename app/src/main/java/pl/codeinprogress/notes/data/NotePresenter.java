package pl.codeinprogress.notes.data;

import android.content.Intent;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;

import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.firebase.FirebaseActivity;
import pl.codeinprogress.notes.firebase.FirebaseLink;
import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.ui.DetailsActivity;
import pl.codeinprogress.notes.ui.tasks.LoadNoteTask;
import pl.codeinprogress.notes.ui.tasks.SaveNoteTask;

/**
 * Created by tomaszmartin on 26.06.2016.
 */

public class NotePresenter {

    private NoteView noteView;
    private FirebaseActivity activity;
    private DatabaseReference database;
    private StorageReference storage;

    public NotePresenter(NoteView noteView, FirebaseActivity activity) {
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
        noteView.noteAdded(note);
    }

    public void saveNote(Note note, String contents) {
        SaveNoteTask saveNoteTask = new SaveNoteTask(activity, note.getFileName());
        saveNoteTask.execute(contents);
    }

    public void getNoteContent(String noteId) {
        StorageReference fileReference = storage.child(noteId);
    }

    public void getNote(String noteId) {
        DatabaseReference noteReference = database.child(noteId);
        noteReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Note note = dataSnapshot.getValue(Note.class);
                noteView.viewNote(note);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void deleteNote(Note note) {
        DatabaseReference noteReference = activity.getDatabase().getReference(FirebaseLink.forNote(note));
        noteReference.removeValue();
        noteView.noteDeleted(note);
    }

    public void openNote(Note note) {
        String noteId = note.getId();
        Intent openNote = new Intent(activity, DetailsActivity.class);
        openNote.putExtra(DetailsActivity.NOTE_ID, noteId);
        activity.startActivity(openNote);
    }

}
