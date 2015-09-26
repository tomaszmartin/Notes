package pl.tomaszmartin.stuff;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import pl.tomaszmartin.stuff.data.NotesContract;

public class MainActivity extends AnalyticsActivity implements OnSelectListener, OnAddListener {

    // TODO: add support for tablet layout
    private boolean isTwoPane = false;
    private final String DETAILS_TAG = DetailsFragment.class.getSimpleName();
    private final String TAG = MainActivity.class.getSimpleName();
    @Bind(R.id.drawer) DrawerLayout drawer;
    private Fragment fragment;
    private MenuItem searchItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        ButterKnife.bind(this);
        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(buildPolicy());
        }

        // Set toolbar as the action bar
        // TODO: on API 16 in ActionMode color is mixed
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up navigation
        //drawer = (DrawerLayout) findViewById(R.id.drawer);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationListener(this, drawer));

        // Set the menu icon in toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        }

        attachFragment(null);

    }

    private void setupViewPager(final ViewPager viewPager) {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MainFragment(), getString(R.string.all));
        adapter.addFragment(new MainFragment(), getString(R.string.personal));
        adapter.addFragment(new MainFragment(), getString(R.string.work));
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {

        public final List<Fragment> mFragmentList = new ArrayList<>();
        public final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
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
        }
    }

    // For a ViewPager configuration
    private void setupViewPager(int pagerId, int tabLayoutId) {
        ViewPager viewPager = (ViewPager) findViewById(pagerId);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(tabLayoutId);
        tabLayout.setupWithViewPager(viewPager);

        drawer = (DrawerLayout) findViewById(R.id.drawer);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(new NavigationListener(this, drawer));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchItem = menu.findItem(R.id.action_search);
        SearchView searchView =
                (SearchView) searchItem.getActionView();

        // Not used right now
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                attachFragment(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //attachFragment(newText);
                return false;
            }
        }); // TODO: implement OnQueryTextListener

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            drawer.openDrawer(Gravity.LEFT);
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
                .setLabel(TAG)
                .build());
    }

    private void attachFragment(String query) {

        Bundle bundle = new Bundle();
        if (query != null && !query.isEmpty()) {
            bundle.putString(NotesContract.NoteEntry.COLUMN_TITLE, query);
        }
        Fragment currFragment = getSupportFragmentManager().findFragmentByTag(TAG);
        if (currFragment != null) {
            getSupportFragmentManager().beginTransaction().remove(currFragment).commit();
        }
        fragment = new MainFragment();
        fragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, fragment, TAG)
                .commit();

    }

    public Fragment getFragment() {
        return fragment;
    }

    @Override
    public void onItemAdded(int id) {
        onItemSelected(id);
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        super.onTrimMemory(level);
    }

}
