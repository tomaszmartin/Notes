package pl.tomaszmartin.stuff.ui;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.analytics.HitBuilders;

import pl.tomaszmartin.stuff.AnalyticsActivity;
import pl.tomaszmartin.stuff.AnalyticsApplication;
import pl.tomaszmartin.stuff.BuildConfig;
import pl.tomaszmartin.stuff.OnSelectListener;
import pl.tomaszmartin.stuff.R;
import pl.tomaszmartin.stuff.data.NotesContract;

/**
 * Created by tomaszmartin on 12.07.2015.
 */
public class SearchActivity extends AnalyticsActivity implements OnSelectListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_activity);

        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(buildPolicy());
        }

        // Set toolbar as the action bar
        // TODO: on API 16 in ActionMode color is mixed
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set the menu icon in toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_arrow_left_white_24dp);
        }

        String query = getIntent().getStringExtra(SearchManager.QUERY);
        attachFragment(query);

    }

    @Override
    protected void onNewIntent(Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_SEARCH)) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            attachFragment(query);
        } else if (intent.getAction().equals(Intent.ACTION_VIEW)) {
            Uri data = intent.getData();
            int id = Integer.parseInt(data.getLastPathSegment());
            onItemSelected(id);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView =
                (SearchView) searchItem.getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchItem.expandActionView();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private StrictMode.ThreadPolicy buildPolicy() {
        return (new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
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

    private void attachFragment(String query) {

        Bundle bundle = new Bundle();
        if (query != null && !query.isEmpty()) {
            bundle.putString(NotesContract.NoteEntry.COLUMN_TITLE, query);
        }
        Fragment currFragment = getSupportFragmentManager().findFragmentByTag(getTag());
        if (currFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(currFragment).commit();
        }
        Fragment fragment = new SearchFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment, getTag())
                .commit();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
