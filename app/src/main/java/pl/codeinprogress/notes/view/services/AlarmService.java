package pl.codeinprogress.notes.view.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.view.DetailsActivity;
import pl.codeinprogress.notes.presenter.DetailsPresenter;
import pl.codeinprogress.notes.view.views.DetailsView;

public class AlarmService extends Service implements DetailsView {

    @Override
    public IBinder onBind(Intent arg) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        String noteId = intent.getStringExtra(DetailsActivity.NOTE_ID);
        DetailsPresenter presenter = new DetailsPresenter(this, null);
        presenter.getNote(noteId);

        return START_NOT_STICKY;
    }

    private PendingIntent createPendingIntent(String id) {
        Intent details = new Intent(this.getApplicationContext(), DetailsActivity.class);
        details.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        return PendingIntent.getActivity(this.getApplicationContext(), id.hashCode(), details, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void noteLoaded(Note note) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification =  new NotificationCompat.Builder(this)
                .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_LIGHTS | Notification.DEFAULT_VIBRATE)
                .setSmallIcon(R.drawable.ic_bell_white_24dp)
                .setContentTitle(note.getTitle())
                .setContentText(note.getDescription())
                .setColor(getResources().getColor(R.color.primary))
                .setContentIntent(createPendingIntent(note.getId()))
                .build();
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        manager.notify(note.getId().hashCode(), notification);
    }

    @Override
    public void noteContentsLoaded(String contents) {

    }
}
