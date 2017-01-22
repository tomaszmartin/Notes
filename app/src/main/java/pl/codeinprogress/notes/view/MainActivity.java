package pl.codeinprogress.notes.view;import android.content.Intent;import android.os.Bundle;import android.support.design.widget.FloatingActionButton;import android.support.design.widget.NavigationView;import android.support.v4.view.GravityCompat;import android.support.v4.widget.DrawerLayout;import android.support.v7.widget.SearchView;import android.support.v7.widget.Toolbar;import android.text.Html;import android.view.ActionMode;import android.view.Menu;import android.view.MenuItem;import android.view.View;import android.widget.AbsListView;import android.widget.AdapterView;import android.widget.ArrayAdapter;import android.widget.Button;import android.widget.ListView;import java.util.ArrayList;import pl.codeinprogress.notes.R;import pl.codeinprogress.notes.model.Note;import pl.codeinprogress.notes.presenter.MainPresenter;import pl.codeinprogress.notes.view.listeners.NavigationListener;import pl.codeinprogress.notes.view.views.MainView;public class MainActivity extends BaseActivity implements MainView,        AdapterView.OnItemClickListener, AbsListView.MultiChoiceModeListener {    private MainPresenter presenter;    private ArrayList<Note> selectedNotes;    private ArrayAdapter<Note> adapter;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        setContentView(R.layout.activity_main);        selectedNotes = new ArrayList<>();        setupView();    }    @Override    protected void onResume() {        super.onResume();        setupData();    }    @Override    protected void onNewIntent(Intent intent) {        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_VIEW)) {            String noteId = intent.getStringExtra(DetailsActivity.NOTE_ID);            presenter.openNote(noteId);        }    }    @Override    public boolean onCreateOptionsMenu(Menu menu) {        getMenuInflater().inflate(R.menu.menu_main, menu);        final MenuItem searchItem = menu.findItem(R.id.action_search);        final SearchView searchView = (SearchView) searchItem.getActionView();        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {            @Override            public boolean onQueryTextSubmit(String query) {                return true;            }            @Override            public boolean onQueryTextChange(String query) {                presenter.search(query);                return true;            }        });        return true;    }    @Override    public boolean onOptionsItemSelected(MenuItem item) {        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);        ListView listView = (ListView) findViewById(R.id.listView);        int id = item.getItemId();        if (id == R.id.action_sort_title) {            presenter.sortByTitle();            return true;        } else if (id == R.id.action_sort_newest) {            presenter.sortByDate();            return true;        } else if (id == android.R.id.home) {            drawerLayout.openDrawer(GravityCompat.START);        } else if (id == R.id.action_select_all) {            for (int i = 0; i < listView.getChildCount(); i++) {                listView.setItemChecked(i, true);            }        } else if (id == R.id.action_night_mode) {            switchNightMode();        }        return super.onOptionsItemSelected(item);    }    private void setupView() {        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);        ListView listView = (ListView) findViewById(R.id.listView);        View emptyView = findViewById(R.id.emptyList);        listView.setEmptyView(emptyView);        listView.setMultiChoiceModeListener(this);        listView.setOnItemClickListener(this);        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);        setSupportActionBar(toolbar);        if (getSupportActionBar() != null) {            getSupportActionBar().setDisplayHomeAsUpEnabled(true);            toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);        }    }    private void setupData() {        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);        NavigationView navigationView = (NavigationView) findViewById(R.id.navigationView);        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);        NavigationListener navigationListener = new NavigationListener(this, drawerLayout);        navigationView.setNavigationItemSelectedListener(navigationListener);        fab.setOnClickListener(v -> presenter.addNote());        presenter = new MainPresenter(this, this);        presenter.loadNotes();    }    @Override    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {        log("onItemClick called");        if (parent.getItemAtPosition(position) instanceof Note) {            Note clickedNote = (Note) parent.getItemAtPosition(position);            presenter.openNote(clickedNote);        }    }    @Override    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {        Note note = adapter.getItem(position);        if (checked) {            selectedNotes.add(note);        } else {            if (selectedNotes.contains(note)) {                selectedNotes.remove(note);            }        }        setActionModeTitle(mode, selectedNotes.size());    }    @Override    public boolean onCreateActionMode(ActionMode mode, Menu menu) {        getMenuInflater().inflate(R.menu.context_menu, menu);        return true;    }    @Override    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {        return false;    }    @Override    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {        if (item.getItemId() == R.id.action_delete) {            for (Note note: selectedNotes) {                presenter.deleteNote(note);            }        }        mode.finish();        return false;    }    @Override    public void onDestroyActionMode(ActionMode mode) {        selectedNotes.clear();    }    private void setActionModeTitle(ActionMode mode, int numberOfItemsSelected) {        String quantityString = getResources().getQuantityString(R.plurals.selected_notes,                numberOfItemsSelected);        mode.setTitle(Html.fromHtml(String.format(String.valueOf("<small>" + quantityString + "</small>"), numberOfItemsSelected)));    }    @Override    public void showNotes(ArrayList<Note> notes) {        for (Note note: notes) {            log(note.toString());        }        // todo: add adapter    }}