package pl.tomaszmartin.stuff;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.util.Pair;

import pl.tomaszmartin.stuff.data.NotesContract.NoteEntry;
import pl.tomaszmartin.stuff.ui.DetailsActivity;

/**
 * Created by tomaszmartin on 03.05.15.
 */

public class AlarmService extends Service {

    @Override
    public IBinder onBind(Intent arg) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        int id = intent.getIntExtra(NoteEntry.COLUMN_ID, 0);
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = createNotification(id);
        manager.notify(id, notification);

        return START_NOT_STICKY;
    }

    private PendingIntent createPendingIntent(int id) {
        Intent details = new Intent(this.getApplicationContext(), DetailsActivity.class);
        details.putExtra(NoteEntry.COLUMN_ID, id);
        details.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        return PendingIntent.getActivity(this.getApplicationContext(), id, details, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private Notification createNotification(int id) {
        String title = "";
        String description = "";

        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(NoteEntry.buildNoteUri(id), null, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            title = cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_TITLE));
            description = cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_DESCRIPTION));
            cursor.close();
        }

        Notification notification =  new NotificationCompat.Builder(this)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.drawable.ic_bell_white_24dp)
                .setContentTitle(title)
                .setContentText(description)
                .setColor(getResources().getColor(R.color.primary))
                .setContentIntent(createPendingIntent(id))
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;


        return notification;
    }

}
