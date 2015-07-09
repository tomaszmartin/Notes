package pl.tomaszmartin.stuff;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import pl.tomaszmartin.stuff.NotesContract.NoteEntry;

/**
 * Created by tomaszmartin on 22.06.2015.
 */
public class DrawingActivity extends AnalyticsActivity {

    private String TAG  = DrawingActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set null background to minimize overdraw
        getWindow().setBackgroundDrawable(null);

        setContentView(R.layout.drawing_activity);
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
            fragment = new DrawingFragment();
            fragment.setArguments(bundle);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, fragment, TAG)
                    .commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_drawing, menu);

        return true;
    }

}
