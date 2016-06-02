package pl.codeinprogress.notes;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import pl.codeinprogress.notes.data.NotesContract.NoteEntry;

/**
 * Created by tomaszmartin on 03.05.15.
 */

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra(NoteEntry.COLUMN_ID, 0);

        Intent service = new Intent(context, AlarmService.class);
        service.putExtra(NoteEntry.COLUMN_ID, id);
        context.startService(service);
    }
}