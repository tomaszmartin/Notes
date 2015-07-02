package pl.tomaszmartin.stuff;

import android.content.Intent;
import android.os.Build;
import android.os.StrictMode;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AnalyticsActivity implements SelectionListener {

    // TODO: add support for tablet layout
    private boolean isTwoPane = false;
    private final String DETAILS_TAG = DetailsFragment.class.getSimpleName();
    private final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if (BuildConfig.DEBUG && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(buildPolicy());
        }

        // Check if the app is in two pane mode (sw600dp)
        if (findViewById(R.id.details_container) != null) {
            isTwoPane = true;
        }

        // Set toolbar as the action bar
        // TODO: on API 16 in ActionMode color is mixed
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setting up view pager and tab layout
        ViewPager viewPager = (ViewPager) findViewById(R.id.pager);
        setupViewPager(viewPager);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // If the app is in two pane mode add details fragment
        // and whether the fragemnt has already been created
        if (isTwoPane && savedInstanceState != null) {
            Bundle bundle = new Bundle();
            bundle.putInt(NotesContract.NoteEntry.COLUMN_ID, 0);


            Fragment fragment = getSupportFragmentManager().findFragmentByTag(DETAILS_TAG);
            if (fragment == null) {
                fragment = new DetailsFragment();
            }

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.details_container, fragment, DETAILS_TAG)
                    .commit();
        }

        // Set the menu icon in toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.mipmap.ic_menu);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private StrictMode.ThreadPolicy buildPolicy() {
        return (new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog().build());
    }

    private void setupViewPager(final ViewPager viewPager) {
        final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new MainFragment(), getString(R.string.all));
        adapter.addFragment(new MainFragment(), getString(R.string.notes));
        adapter.addFragment(new MainFragment(), getString(R.string.articles));
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onItemSelected(int id) {
        if (isTwoPane) {
            Bundle bundle = new Bundle();
            bundle.putInt(NotesContract.NoteEntry.COLUMN_ID, id);
            DetailsFragment fragment = new DetailsFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.details_container, fragment, DETAILS_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailsActivity.class);
            intent.putExtra(NotesContract.NoteEntry.COLUMN_ID, id);
            startActivity(intent);
        }

        ((AnalyticsApplication) getApplication()).getDefaultTracker().send(new HitBuilders.EventBuilder()
                .setCategory("Note selected")
                .setAction("Note id: " + id)
                .setLabel(TAG)
                .build());
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
}
