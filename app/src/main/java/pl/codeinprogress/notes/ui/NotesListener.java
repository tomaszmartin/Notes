package pl.codeinprogress.notes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.google.firebase.database.DatabaseReference;

import pl.codeinprogress.notes.firebase.LinkBuilder;
import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.firebase.FirebaseActivity;

/**
 * Created by tomaszmartin on 23.06.2016.
 */

public class NotesListener implements View.OnClickListener, AdapterView.OnItemClickListener {

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

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getItemAtPosition(position) instanceof Note) {
            Note clickedNote = (Note) parent.getItemAtPosition(position);
            openNote(clickedNote);
        }
    }

    private void openNote(Note note) {
        String noteId = note.getId();
        Intent openNote = new Intent(activity, DetailsActivity.class);
        openNote.putExtra(DetailsActivity.NOTE_ID, noteId);
        activity.startActivity(openNote);
    }
}
