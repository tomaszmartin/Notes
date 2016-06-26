package pl.codeinprogress.notes.ui;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.google.firebase.database.DatabaseReference;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.adapters.NotesAdapter;
import pl.codeinprogress.notes.data.NotesProvider;
import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.firebase.FirebaseActivity;
import pl.codeinprogress.notes.firebase.FirebaseLink;
import pl.codeinprogress.notes.ui.listeners.NavigationListener;
import pl.codeinprogress.notes.ui.listeners.NotesListener;
import pl.codeinprogress.notes.ui.listeners.OnAddListener;
import pl.codeinprogress.notes.ui.listeners.OnSelectListener;

public class MainActivity extends FirebaseActivity implements OnSelectListener, OnAddListener {

    @Bind(R.id.navigationView) NavigationView navigationView;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.drawerLayout) DrawerLayout drawerLayout;
    @Bind(R.id.fab) FloatingActionButton fab;
    @Bind(R.id.listView) ListView listView;
    @Bind(R.id.emptyList) View emptyList;
    @Bind(R.id.clearSearchutton) ImageButton clearSearchutton;
    private NotesListener notesListener;
    private NotesAdapter adapter;
    private MenuItem searchItem;
    private NotesProvider provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        ButterKnife.bind(this);

        setupView();
        setupData();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_VIEW)) {
            Uri data = intent.getData();
            int id = Integer.parseInt(data.getLastPathSegment());
            // onItemSelected(id);
        } else if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_REBOOT)) {
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_sort_title) {
            return true;
        } else if (id == R.id.action_sort_newest) {
            return true;
        } else if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START);
        } else if (id == R.id.action_select_all) {

        } else if (id == R.id.action_night_mode) {
            switchNightMode();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(Note note) {
        provider.openNote(note);
    }

    @Override
    public void onItemAdded(Note note) {
        provider.openNote(note);
    }

    @Override
    public void authenticate() {
        Intent loginIntenet = new Intent(this, LoginActivity.class);
        startActivity(loginIntenet);
    }

    private void setupView() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        }
    }

    private void setupData() {
        provider = new NotesProvider(this);
        DatabaseReference notesReference = getDatabase().getReference(FirebaseLink.forNotes());
        NavigationListener navigationListener = new NavigationListener(this, drawerLayout);

        adapter = new NotesAdapter(this, Note.class, R.layout.main_item, notesReference);
        notesListener = new NotesListener(this, adapter);

        listView.setAdapter(adapter);
        listView.setEmptyView(emptyList);
        listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        navigationView.setNavigationItemSelectedListener(navigationListener);
        listView.setMultiChoiceModeListener(notesListener);
        listView.setOnItemClickListener(notesListener);
        fab.setOnClickListener(notesListener);
        clearSearchutton.setOnClickListener(null);
    }

}
