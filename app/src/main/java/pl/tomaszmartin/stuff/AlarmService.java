package pl.tomaszmartin.stuff;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import pl.tomaszmartin.stuff.data.NotesContract.NoteEntry;

/**
 * Created by tomaszmartin on 03.05.15.
 */

public class AlarmService extends Service {

    private NotificationManager manager;
    private final String TAG = AlarmService.class.getSimpleName();
    public static final String TIME = "alarm_time";

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override public void onCreate()  {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        // Retrieving all the values and objects
        int id = intent.getIntExtra(NoteEntry.COLUMN_ID, 0);
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(NoteEntry.buildNoteUri(id), null, null, null, null);
        String title = "";
        String description = "";
        if (cursor != null && cursor.moveToFirst()) {
            title = cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_TITLE));
            description = cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_DESCRIPTION));
        }
        manager = (NotificationManager) this.getApplicationContext().
                getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);

        // Creating intent for alarm
        Intent details = new Intent(this.getApplicationContext(), DetailsActivity.class);
        details.putExtra(NoteEntry.COLUMN_ID, id);
        details.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingNotificationIntent = PendingIntent
                .getActivity(this.getApplicationContext(), id, details, PendingIntent.FLAG_UPDATE_CURRENT);

        // Setting the notification
        Notification notification = new NotificationCompat.Builder(this)
                .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.drawable.ic_bell_white_24dp)
                .setContentTitle(title)
                .setContentText(description)
                .setColor(getResources().getColor(R.color.primary))
                .setContentIntent(pendingNotificationIntent)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(id, notification);

        return START_NOT_STICKY;
    }

    @Override public void onDestroy()  {
        super.onDestroy();
     }

}
