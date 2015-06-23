package pl.tomaszmartin.stuff;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import pl.tomaszmartin.stuff.NotesContract.NoteEntry;


public class DetailsActivity extends AppCompatActivity {

    private String TAG = DetailsActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        // Set toolbar as the action bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }

        int id = getIntent().getIntExtra(NoteEntry.COLUMN_ID, -1);
        attachFragment(id);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        int noteId = getIntent().getIntExtra(NoteEntry.COLUMN_ID, -1);

        attachFragment(noteId);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
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
        DetailsFragment detailsFragment = new DetailsFragment();
        detailsFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.container, detailsFragment)
                .commit();
    }

    private void setAlarm(long time, int id) {
        Log.d(TAG, "action alarm called with " + id);

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("id", id);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, id, intent, 0);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                time,
                alarmIntent);

    }

}
