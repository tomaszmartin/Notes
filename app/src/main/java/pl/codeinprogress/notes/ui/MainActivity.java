package pl.codeinprogress.notes.ui;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import com.google.firebase.database.DatabaseReference;
import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.adapters.NotesAdapter;
import pl.codeinprogress.notes.data.NotePresenter;
import pl.codeinprogress.notes.databinding.MainActivityBinding;
import pl.codeinprogress.notes.firebase.FirebaseActivity;
import pl.codeinprogress.notes.firebase.FirebaseLink;
import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.ui.listeners.NavigationListener;
import pl.codeinprogress.notes.ui.listeners.NotesListener;
import pl.codeinprogress.notes.ui.listeners.OnAddListener;
import pl.codeinprogress.notes.ui.listeners.OnSelectListener;

public class MainActivity extends FirebaseActivity implements OnSelectListener, OnAddListener {

    private NotesListener notesListener;
    private NotesAdapter adapter;
    private MenuItem searchItem;
    private NotePresenter provider;
    private MainActivityBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity);

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
            binding.drawerLayout.openDrawer(GravityCompat.START);
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
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            binding.toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        }
    }

    private void setupData() {
        provider = new NotePresenter(this);
        DatabaseReference notesReference = getDatabase().getReference(FirebaseLink.forNotes());
        NavigationListener navigationListener = new NavigationListener(this, binding.drawerLayout);

        adapter = new NotesAdapter(this, Note.class, R.layout.main_item, notesReference);
        notesListener = new NotesListener(this, adapter);

        binding.listView.setAdapter(adapter);
        binding.listView.setEmptyView(binding.emptyList);
        binding.listView.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE_MODAL);
        binding.navigationView.setNavigationItemSelectedListener(navigationListener);
        binding.listView.setMultiChoiceModeListener(notesListener);
        binding.listView.setOnItemClickListener(notesListener);
        binding.fab.setOnClickListener(notesListener);
        binding.clearSearchutton.setOnClickListener(null);
    }

}
