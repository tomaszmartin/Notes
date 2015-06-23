package pl.tomaszmartin.stuff;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Created by tomaszmartin on 03.05.15.
 */
public class AlarmService extends Service {

    private NotificationManager manager;
    private String tag = "___AlarmService";

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub return null;
        return null;
    }

    @Override public void onCreate()  {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        int noteId = intent.getIntExtra("id", 0);
        Log.d(tag, "onStartCommand() called with " + noteId);
        String noteTitle = NotesFactory.get(this).getNote(noteId).getTitle();
        String noteDescription = NotesFactory.get(this).getNote(noteId).getDescription();

        manager = (NotificationManager) this.getApplicationContext().
                getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);

        Intent details = new Intent(this.getApplicationContext(), DetailsActivity.class);
        details.putExtra("id", noteId);
        details.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP| Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingNotificationIntent = PendingIntent
                .getActivity(this.getApplicationContext(), noteId, details, PendingIntent.FLAG_UPDATE_CURRENT);

        Notification notification = new NotificationCompat.Builder(this)
                .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_LIGHTS|Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.mipmap.ic_bell)
                .setContentTitle(noteTitle)
                .setContentText(noteDescription)
                .setColor(getResources().getColor(R.color.primary))
                .setContentIntent(pendingNotificationIntent)
                .build();

        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(noteId, notification);

        return START_NOT_STICKY;
    }

    @Override public void onDestroy()  {
        super.onDestroy();
     }

}
