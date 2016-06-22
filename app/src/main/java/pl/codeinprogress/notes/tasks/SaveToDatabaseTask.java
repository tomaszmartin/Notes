package pl.codeinprogress.notes.tasks;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;

/**
 * Created by tomaszmartin on 15.07.2015.
 */
public class SaveToDatabaseTask extends AsyncTask<ContentValues, Void, Void> {

    private Context context;
    private Uri path;

    public SaveToDatabaseTask(Context context, Uri path) {
        this.context = context.getApplicationContext();
        this.path = path;
    }

    private void saveToDatabase(ContentValues values) {
        if (values != null && values.size() > 0 && path != null) {
            context.getContentResolver().update(path, values, null, null);
        }
    }

    private void saveToFirebase(ContentValues values) {
        if (values != null && values.size() > 0 && path != null) {
            context.getContentResolver().update(path, values, null, null);
        }
    }

    @Override
    protected Void doInBackground(ContentValues... params) {
        saveToDatabase(params[0]);

        return null;
    }

}
