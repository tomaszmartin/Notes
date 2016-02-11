package pl.tomaszmartin.stuff.ui;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;

import pl.tomaszmartin.stuff.R;
import pl.tomaszmartin.stuff.data.NotesContract.NoteEntry;


public class DetailsActivity extends AnalyticsActivity {

    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        // For better performance and avoiding overdraw
        getWindow().setBackgroundDrawable(null);

        // Set toolbar as the action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }

        int id = getIntent().getIntExtra(NoteEntry.COLUMN_ID, 0);
        if (savedInstanceState == null) {
            attachFragment(id);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_details, menu);

        return true;
    }

    private void attachFragment(int id) {

        Bundle bundle = new Bundle();
        bundle.putInt(NoteEntry.COLUMN_ID, id);
        fragment = getSupportFragmentManager().findFragmentByTag(getTag());
        if (fragment == null) {
            fragment = new DetailsFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment, getTag())
                    .commit();
        }
    }

    public Fragment getFragment() {
        return fragment;
    }

}
