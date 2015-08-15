package pl.tomaszmartin.stuff;

import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Html;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by tomaszmartin on 12.07.2015.
 */
public class SearchFragment extends Fragment
        implements android.support.v4.app.LoaderManager.LoaderCallbacks<Cursor>,
        AbsListView.MultiChoiceModeListener, View.OnClickListener {

    private String TAG = SearchFragment.class.getSimpleName();
    private static final int NOTES_LOADER = 0;
    private NotesAdapter adapter;
    private ListView listView;
    private View rootView;
    private ArrayList<Long> selectedPositions = new ArrayList<>();
    private int numberOfItemsSelected;

    private static final String[] NOTES_COLUMNS = NotesContract.NoteEntry.NOTE_COLUMNS;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Set up the initial view
        rootView = inflater.inflate(R.layout.search_fragment, container, false);
        listView = (ListView) rootView.findViewById(R.id.grid);

        // Set view for an empty list view
        RelativeLayout emptyView = (RelativeLayout) rootView.findViewById(R.id.empty_list);
        listView.setEmptyView(emptyView);

        adapter = new NotesAdapter(getActivity(), null, 0);
        listView.setAdapter(adapter);

        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Cursor cursor = adapter.getCursor();
                if (cursor != null && cursor.moveToPosition(position)) {
                    int noteId = cursor.getInt(cursor.getColumnIndex(NotesContract.NoteEntry.COLUMN_ID));
                    ((OnSelectListener) getActivity()).onItemSelected(noteId);
                }
            }
        });

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(NOTES_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public void deleteNote(int position) {
        Cursor cursor = adapter.getCursor();
        if (cursor != null && cursor.moveToPosition(position)) {
            int noteId = cursor.getInt(cursor.getColumnIndex(NotesContract.NoteEntry.COLUMN_ID));
            DeleteNoteTask deleteNoteTask = new DeleteNoteTask(getActivity());
            deleteNoteTask.execute(noteId);

            // Google Analytics event tracking
            ((AnalyticsApplication) getActivity().getApplication()).getDefaultTracker().send(new HitBuilders.EventBuilder()
                    .setCategory("Note deleted")
                    .setAction("Note id " + position)
                    .setLabel(TAG)
                    .build());
        }
    }

    public void showSnackbar(String text, String action) {
        Snackbar.make(getActivity().findViewById(R.id.coordinator), text, Snackbar.LENGTH_LONG)
                .setActionTextColor(getResources().getColor(R.color.red_500))
                .setAction(action, undoDeleteNotesListener)
                .show();
    }

    View.OnClickListener undoDeleteNotesListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            // TODO: implement archiving method
        }
    };

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {

        // Sort by date of creation, ascending
        String sortOrder = NotesContract.NoteEntry.COLUMN_DATE_CREATED + " ASC";
        Uri notesUri;
        if (getArguments() != null && getArguments().getString(NotesContract.NoteEntry.COLUMN_TITLE) != null) {
            String query = getArguments().getString(NotesContract.NoteEntry.COLUMN_TITLE, "");
            notesUri = NotesContract.NoteEntry.buildQueryUri(query);
            Log.d(TAG, "loader created with uri: " + notesUri.toString());
        } else {
            notesUri = NotesContract.NoteEntry.buildAllNotesUri();
            Log.d(TAG, "loader created without query");
        }

        return new CursorLoader(getActivity(),
                notesUri,
                NOTES_COLUMNS,
                null,
                null,
                sortOrder);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        if (checked) {
            selectedPositions.add(Long.valueOf(position));
            numberOfItemsSelected++;
        } else {
            selectedPositions.remove(Long.valueOf(position));
            numberOfItemsSelected--;
        }

        setActionModeTitle(mode, numberOfItemsSelected);
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        getActivity().getMenuInflater().inflate(R.menu.context_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        if (item.getItemId() == R.id.action_delete) {
            //TODO: change this method, its ugly

            ArrayList<Integer> positionsToDelete = new ArrayList<Integer>();
            for (Long position : selectedPositions) {
                positionsToDelete.add(position.intValue());
            }

            // Sorts the array in ascending order
            Collections.sort(positionsToDelete);

            for (Integer position : positionsToDelete) {
                //TODO: implement method for archiving notes
                deleteNote(position);
            }

            showSnackbar(getString(R.string.deleted_multiple), getString(R.string.undo));
        }

        mode.finish();
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        selectedPositions = new ArrayList<Long>();
        numberOfItemsSelected = 0;
    }

    // Set title of the action mode according to number of items selected
    private void setActionModeTitle(ActionMode mode, int numberOfItemsSelected) {
        if (numberOfItemsSelected == 0) {
            mode.setTitle("");
        } else if (numberOfItemsSelected == 1) {
            mode.setTitle(Html.fromHtml("<small>" + String.valueOf(numberOfItemsSelected) + " " + getString(R.string.one_note_chosen) + "</small>"));
        } else if (numberOfItemsSelected == 2 || numberOfItemsSelected == 3 || numberOfItemsSelected == 4) {
            mode.setTitle(Html.fromHtml("<small>" + String.valueOf(numberOfItemsSelected) + " " + getString(R.string.two_note_chosen) + "</small>"));
        } else {
            mode.setTitle(Html.fromHtml("<small>" + String.valueOf(numberOfItemsSelected) + " " + getString(R.string.five_note_chosen) + "</small>"));
        }
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.fab) {
            addNote();
        }
    }

    private void addNote() {
        AddNoteTask task = new AddNoteTask(getActivity());
        task.execute(null, null, null);
        // Google Analytics event tracking

        ((AnalyticsApplication) getActivity().getApplication()).getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Note added")
                .setAction("New note")
                .setLabel(TAG)
                .build());
    }

}