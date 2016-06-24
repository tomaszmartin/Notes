package pl.codeinprogress.notes.ui;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.firebase.FirebaseActivity;


public class DetailsActivity extends FirebaseActivity {

    private Fragment fragment;
    public static String NOTE_ID = "noteid";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }

        //int id = getIntent().getIntExtra(NoteEntry.COLUMN_ID, 0);
//        if (savedInstanceState == null) {
//            attachFragment(id);
//        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);

        return true;
    }

    private void attachFragment(int id) {
        Bundle bundle = new Bundle();
        // bundle.putInt(NoteEntry.COLUMN_ID, id);
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
