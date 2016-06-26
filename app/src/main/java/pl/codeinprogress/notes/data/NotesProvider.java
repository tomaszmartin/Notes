package pl.codeinprogress.notes.data;

import android.content.Intent;
import com.google.firebase.database.DatabaseReference;
import pl.codeinprogress.notes.firebase.FirebaseActivity;
import pl.codeinprogress.notes.firebase.FirebaseLink;
import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.ui.DetailsActivity;
import pl.codeinprogress.notes.ui.tasks.SaveNoteTask;

/**
 * Created by tomaszmartin on 26.06.2016.
 */

public class NotesProvider {

    private FirebaseActivity activity;

    public NotesProvider(FirebaseActivity activity) {
        this.activity = activity;
    }

    public void addNote() {
        DatabaseReference reference = activity.getDatabase().getReference(FirebaseLink.forNotes());
        DatabaseReference noteReference = reference.push();
        String noteId = noteReference.getKey();
        Note note = new Note(noteId);
        noteReference.setValue(note);
    }

    public void saveNote(Note note, String contents) {
        SaveNoteTask saveNoteTask = new SaveNoteTask(activity, note.getFileName());
        saveNoteTask.execute(contents);
    }

    public void loadNote() {

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
