package pl.tomaszmartin.stuff;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by tomaszmartin on 21.06.2015.
 */

public class SaveToFileTask extends AsyncTask<String, Void, Void> {

    private Context context;
    private String path;

    public SaveToFileTask(Context context, String path) {
        this.context = context.getApplicationContext();
        this.path = path;
    }

    private void saveToFile(String contents) {
        if (!contents.isEmpty() && !path.isEmpty()) {
            try {
                FileOutputStream fos = context.openFileOutput(path, Activity.MODE_PRIVATE);
                fos.write(contents.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected Void doInBackground(String... params) {
        saveToFile(params[0]);

        return null;
    }

}
