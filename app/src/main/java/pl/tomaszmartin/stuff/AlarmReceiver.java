package pl.tomaszmartin.stuff;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import pl.tomaszmartin.stuff.NotesContract.NoteEntry;

/**
 * Created by tomaszmartin on 03.05.15.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private String TAG = AlarmReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra(NoteEntry.COLUMN_ID, 0);

        Intent service = new Intent(context, AlarmService.class);
        service.putExtra(NoteEntry.COLUMN_ID, id);
        context.startService(service);
    }
}