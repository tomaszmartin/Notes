package pl.codeinprogress.notes.view.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import pl.codeinprogress.notes.view.DetailsActivity;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String noteId = intent.getStringExtra(DetailsActivity.NOTE_ID);
        Intent service = new Intent(context, AlarmService.class);
        service.putExtra(DetailsActivity.NOTE_ID, noteId);
        context.startService(service);
    }
}