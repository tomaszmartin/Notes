package pl.tomaszmartin.stuff;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by tomaszmartin on 03.05.15.
 */

public class AlarmReceiver extends BroadcastReceiver {

    private String tag = "___AlarmReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("id", 0);
        Log.d(tag, "onReceive() called with " + id);
        Intent service = new Intent(context, AlarmService.class);
        service.putExtra("id", id);
        context.startService(service);
    }
}