package pl.tomaszmartin.stuff.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.analytics.HitBuilders;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.tomaszmartin.stuff.NavigationListener;
import pl.tomaszmartin.stuff.OnAddListener;
import pl.tomaszmartin.stuff.OnSelectListener;
import pl.tomaszmartin.stuff.R;
import pl.tomaszmartin.stuff.data.NotesContract;

public class MainActivity extends AnalyticsActivity implements OnSelectListener, OnAddListener {

    @Bind(R.id.navigation) NavigationView navigationView;
    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.drawer) DrawerLayout drawer;
    private Fragment fragment;
    private MenuItem searchItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ButterKnife.bind(this);

        setupView();
        attachFragment(null, MainFragment.SORT_NEWEST);

    }

    private void setupView() {
        navigationView.setNavigationItemSelectedListener(new NavigationListener(this, drawer));
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_SEARCH)) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            Intent searchIntent = new Intent(this, SearchActivity.class);
            searchIntent.putExtra(SearchManager.QUERY, query);
            searchItem.collapseActionView();
            startActivity(searchIntent);
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
        } else if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_VIEW)) {
            Uri data = intent.getData();
            int id = Integer.parseInt(data.getLastPathSegment());
            onItemSelected(id);
        } else if (intent.getAction() != null && intent.getAction().equals(Intent.ACTION_REBOOT)) {
            attachFragment(null, MainFragment.SORT_NEWEST);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        searchItem = menu.findItem(R.id.action_search);
        SearchView searchView =
                (SearchView) searchItem.getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                attachFragment(query, MainFragment.SORT_NEWEST);
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
            attachFragment(null, MainFragment.SORT_TITLE);
            return true;
        } else if (id == R.id.action_sort_newest) {
            attachFragment(null, MainFragment.SORT_NEWEST);
            return true;
        } else if (id == android.R.id.home) {
            drawer.openDrawer(GravityCompat.START);
        } else if (id == R.id.action_select_all) {
            if (fragment != null && fragment instanceof MainFragment) {
                ((MainFragment) fragment).selectAllNotes();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(int id) {
        Intent intent = new Intent(this, DetailsActivity.class);
        intent.putExtra(NotesContract.NoteEntry.COLUMN_ID, id);
        startActivity(intent);

        ((AnalyticsApplication) getApplication()).getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Note selected")
                .setAction("Note id: " + id)
                .setLabel(getTag())
                .build());
    }

    private void attachFragment(String query, int order) {

        Bundle bundle = new Bundle();
        if (query != null && !query.isEmpty()) {
            bundle.putString(NotesContract.NoteEntry.COLUMN_TITLE, query);
        }
        if (order != -1) {
            bundle.putInt(MainFragment.ORDER_KEY, order);
        }
        Fragment currFragment = getSupportFragmentManager().findFragmentByTag(getTag());
        if (currFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(currFragment).commit();
        }
        fragment = new MainFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment, getTag())
                .commit();

    }

    @Override
    public void onItemAdded(int id) {
        onItemSelected(id);
    }

}
