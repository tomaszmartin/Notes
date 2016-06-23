package pl.codeinprogress.notes.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import pl.codeinprogress.notes.adapters.FirebaseNotesAdapter;
import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.firebase.FirebaseFragment;
import pl.codeinprogress.notes.R;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by tomaszmartin on 24.03.15.
 */

public class MainFragment extends FirebaseFragment implements
        AbsListView.MultiChoiceModeListener,
        View.OnClickListener {

    static final String ORDER_KEY = "ORDER";
    static final int SORT_NEWEST = 1;
    static final int SORT_TITLE = 2;
    private FirebaseNotesAdapter notesAdapter;
    private View rootView;
    private ArrayList<Long> selectedPositions = new ArrayList<>();
    private int numberOfItemsSelected;
    private TextView searchResults;
    private View searchBar;
    private ListView listView;
    private View emptyView;
    private ImageButton clearSearchResults;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setupData();

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(this);
        clearSearchResults.setOnClickListener(this);

        return rootView;
    }

    private void setupData() {
        notesAdapter = new FirebaseNotesAdapter(getActivity(), Note.class, R.layout.main_item, getDatabase().getReference());

        listView.setAdapter(notesAdapter);
        listView.setEmptyView(emptyView);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Note item = notesAdapter.getItem(position);
                if (item != null) {
                    ((OnSelectListener) getActivity()).onItemSelected(item.getId());
                }
            }
        });
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    void deleteNote(int position) {

    }

    void showSnackbar(String text) {
        Snackbar.make(getActivity().findViewById(R.id.coordinator), text, Snackbar.LENGTH_LONG)
                .show();
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        if (checked) {
            selectedPositions.add((long) position);
            numberOfItemsSelected++;
        } else {
            selectedPositions.remove((long) position);
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

            ArrayList<Integer> positionsToDelete = new ArrayList<Integer>();
            for (Long position : selectedPositions) {
                positionsToDelete.add(position.intValue());
            }

            Collections.sort(positionsToDelete);

            for (Integer position : positionsToDelete) {
                deleteNote(position);
            }

            showSnackbar(getString(R.string.deleted_multiple));
        }

        mode.finish();
        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        selectedPositions = new ArrayList<>();
        numberOfItemsSelected = 0;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.fab) {
            addNote();
        } else if (id == R.id.clear_button) {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setAction(Intent.ACTION_REBOOT);
            startActivity(intent);
        }
    }

    void addNote() {

    }

    private void setupSearchResultsView(String query) {
        searchBar.setVisibility(View.VISIBLE);
        searchResults.setText(String.format("%s %s", getString(R.string.search_results_label), query));
    }

    void selectAllNotes() {
        for (int i = 0; i < listView.getChildCount(); i++) {
            listView.setItemChecked(i, true);
        }
    }

    private void setActionModeTitle(ActionMode mode, int numberOfItemsSelected) {
        if (numberOfItemsSelected == 0) {
            mode.setTitle("");
        } else if (numberOfItemsSelected == 1) {
            mode.setTitle(Html.fromHtml(String.valueOf("<small>" + numberOfItemsSelected) + " " + getString(R.string.one_note_chosen) + "</small>"));
        } else if (numberOfItemsSelected == 2 || numberOfItemsSelected == 3 || numberOfItemsSelected == 4) {
            mode.setTitle(Html.fromHtml(String.valueOf("<small>" + numberOfItemsSelected) + " " + getString(R.string.two_note_chosen) + "</small>"));
        } else {
            mode.setTitle(Html.fromHtml(String.valueOf("<small>" + numberOfItemsSelected) + " " + getString(R.string.five_note_chosen) + "</small>"));
        }
    }

}