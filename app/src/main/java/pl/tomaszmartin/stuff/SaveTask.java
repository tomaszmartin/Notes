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

public class SaveTask extends AsyncTask<Saveable, Void, Void> {

    @Override
    protected Void doInBackground(Saveable... saveables) {

        Saveable saveable = saveables[0];
        if (saveable.getSaveType() == Saveable.SAVE_DATABASE_AND_FILE) {
            saveToFile(saveable);
            saveToDatabase(saveable);
        } else if (saveable.getSaveType() == Saveable.SAVE_DATABASE) {
            saveToDatabase(saveable);
        } else if (saveable.getSaveType() == Saveable.SAVE_FILE) {
            saveToFile(saveable);
        }

        return null;
    }

    private void saveToFile(Saveable saveable) {
        String contents = saveable.getContents();
        String fileName = saveable.getFileName();
        Context context = saveable.getContext();

        if (!contents.isEmpty() && !fileName.isEmpty()) {
            try {
                FileOutputStream fos = context.openFileOutput(fileName, Activity.MODE_PRIVATE);
                fos.write(contents.getBytes());
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    private void saveToDatabase(Saveable saveable) {
        ContentValues values = saveable.getValues();
        Uri uri = saveable.getUri();
        Context context = saveable.getContext();

        if (values.size() > 0 && uri != null && values != null) {
            context.getContentResolver().update(uri, values, null, null);
        }
    }

}
