package pl.codeinprogress.notes.view.listeners;

import android.text.Html;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import java.util.ArrayList;
import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.view.adapters.NotesAdapter;
import pl.codeinprogress.notes.presenter.firebase.FirebaseActivity;
import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.presenter.MainPresenter;

/**
 * Created by tomaszmartin on 23.06.2016.
 */

public class NotesListener implements View.OnClickListener,
        AdapterView.OnItemClickListener,
        AbsListView.MultiChoiceModeListener {

    private FirebaseActivity activity;
    private NotesAdapter adapter;
    private MainPresenter provider;
    private ArrayList<Note> selectedNotes = new ArrayList<>();

    public NotesListener(FirebaseActivity activity, NotesAdapter adapter) {
        this.activity = activity;
        this.adapter = adapter;
        this.provider = new MainPresenter(null, activity);
    }

    @Override
    public void onClick(View v) {
        provider.addNote();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (parent.getItemAtPosition(position) instanceof Note) {
            Note clickedNote = (Note) parent.getItemAtPosition(position);
            provider.openNote(clickedNote);
        }
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
                provider.deleteNote(note);
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
        mode.setTitle(Html.fromHtml(String.format(String.valueOf("<small>" + quantityString + "</small>"), numberOfItemsSelected)));
    }

}
