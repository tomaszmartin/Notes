package pl.codeinprogress.notes.ui.presenters;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.data.firebase.FirebaseActivity;
import pl.codeinprogress.notes.data.firebase.FirebaseLink;
import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.ui.tasks.LoadNoteTask;
import pl.codeinprogress.notes.ui.tasks.SaveNoteTask;
import pl.codeinprogress.notes.ui.views.DetailsView;

/**
 * Created by tomaszmartin on 05.07.2016.
 */

public class DetailsPresenter {

    private DetailsView detailsView;
    private FirebaseActivity activity;
    private DatabaseReference database;
    private StorageReference storage;

    public DetailsPresenter(DetailsView detailsView, FirebaseActivity activity) {
        this.detailsView = detailsView;
        this.activity = activity;
        database = FirebaseDatabase.getInstance().getReference(FirebaseLink.forNotes());
        if (activity != null) {
            Alastorage = FirebaseStorage.getInstance().getReferenceFromUrl(activity.getString(R.string.firebase_storage_bucket));
        }
    }

    public void saveNote(Note note, String contents) {
        database.child(note.getId()).setValue(note);
        SaveNoteTask saveNoteTask = new SaveNoteTask(activity, note.getFileName());
        saveNoteTask.execute(contents);
    }

    public void getNoteContent(Note note) {
        LoadNoteTask loadNoteTask = new LoadNoteTask(activity, detailsView);
        loadNoteTask.execute(note);
    }

    public void getNote(String noteId) {
        DatabaseReference noteReference = database.child(noteId);
        noteReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Note note = dataSnapshot.getValue(Note.class);
                if (detailsView != null) {
                    detailsView.noteLoaded(note);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
