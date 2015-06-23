package pl.tomaszmartin.stuff;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import pl.tomaszmartin.stuff.NotesContract.NoteEntry;

/**
 * Created by tomaszmartin on 23.06.15.
 */
public class RecordActivity extends AppCompatActivity {

    private String TAG = RecordActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_activity);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }

        int id = getIntent().getIntExtra(NoteEntry.COLUMN_ID, -1);
        if (savedInstanceState == null) {
            attachFragment(id);
        }
    }

    private void attachFragment(int id) {

        Bundle bundle = new Bundle();
        bundle.putInt(NoteEntry.COLUMN_ID, id);
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(TAG);
        if (fragment == null) {
            fragment = new RecordFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment, TAG)
                    .commit();
        }
    }

}
