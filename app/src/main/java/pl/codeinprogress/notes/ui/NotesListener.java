package pl.codeinprogress.notes.ui;

import android.view.View;

import com.google.firebase.database.DatabaseReference;

import pl.codeinprogress.notes.firebase.LinkBuilder;
import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.firebase.FirebaseActivity;

/**
 * Created by tomaszmartin on 23.06.2016.
 */

public class NotesListener implements View.OnClickListener {

    private FirebaseActivity activity;

    public NotesListener(FirebaseActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onClick(View v) {
        addNote();
    }

    private void addNote() {
        DatabaseReference reference = activity.getDatabase().getReference(LinkBuilder.forNotes());
        DatabaseReference noteReference = reference.push();
        String noteId = noteReference.getKey();
        Note note = new Note(noteId);
        noteReference.setValue(note);
    }

}
