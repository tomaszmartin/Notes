package pl.codeinprogress.notes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.Collections;

import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.adapters.FirebaseNotesAdapter;
import pl.codeinprogress.notes.adapters.NotesAdapter;
import pl.codeinprogress.notes.firebase.LinkBuilder;
import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.firebase.FirebaseActivity;

/**
 * Created by tomaszmartin on 23.06.2016.
 */

public class NotesListener implements View.OnClickListener,
        AdapterView.OnItemClickListener,
        AbsListView.MultiChoiceModeListener {

    private FirebaseActivity activity;
    private FirebaseNotesAdapter adapter;
    private ArrayList<Note> selectedNotes = new ArrayList<>();

    public NotesListener(FirebaseActivity activity, FirebaseNotesAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
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

    public void openNote(Note note) {
        String noteId = note.getId();
        openNote(noteId);
    }

    public void openNote(String noteId) {
        Intent openNote = new Intent(activity, DetailsActivity.class);
        openNote.putExtra(DetailsActivity.NOTE_ID, noteId);
        activity.startActivity(openNote);
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        Note note = adapter.getItem(position);
        if (checked) {
            selectedNotes.add(note);
        } else {
            if (selectedNotes.contains(note)) {
                selectedNotes.remove(note);
            }
        }

        setActionModeTitle(mode, selectedNotes.size());
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        activity.getMenuInflater().inflate(R.menu.context_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            for (Note note: selectedNotes) {
                deleteNote(note);
            }
        }

        mode.finish();
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        selectedNotes.clear();
    }

    private void setActionModeTitle(ActionMode mode, int numberOfItemsSelected) {
        String quantityString = activity.getResources().getQuantityString(R.plurals.selected_notes,
                numberOfItemsSelected);
        mode.setTitle(Html.fromHtml(String.valueOf("<small>" + quantityString + "</small>")));
    }

    private void deleteNote(Note note) {
        DatabaseReference noteReference = activity.getDatabase().getReference(LinkBuilder.forNote(note));
        noteReference.removeValue();
    }

}
