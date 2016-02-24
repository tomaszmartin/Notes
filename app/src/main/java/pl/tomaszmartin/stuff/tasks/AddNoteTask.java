package pl.tomaszmartin.stuff.tasks;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;

import pl.tomaszmartin.stuff.OnAddListener;
import pl.tomaszmartin.stuff.data.NotesContract.NoteEntry;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * Created by tomaszmartin on 15.06.2015.
 */

public class AddNoteTask extends AsyncTask<Void, Void, Integer> {

    private final Context context;

    public AddNoteTask(Context context) {
        this.context = context;
    }

    @Override
    protected Integer doInBackground(Void... voids) {

        String title = "";
        String description = "";
        String fileName = UUID.randomUUID().toString() + ".txt";
        int type = 0;
        long dateCreated = new Date().getTime();
        long dateLastModified = new Date().getTime();
        int category = 0;
        String image = "";

        ContentValues values = new ContentValues();
        values.put(NoteEntry.COLUMN_TITLE, title);
        values.put(NoteEntry.COLUMN_DESCRIPTION, description);
        values.put(NoteEntry.COLUMN_FILE_NAME, fileName);
        values.put(NoteEntry.COLUMN_TYPE, type);
        values.put(NoteEntry.COLUMN_DATE_CREATED, dateCreated);
        values.put(NoteEntry.COLUMN_DATE_LAST_MODIFIED, dateLastModified);
        values.put(NoteEntry.COLUMN_CATEGORY, category);
        values.put(NoteEntry.COLUMN_IMAGE_URI, image);

        Uri insertUri = context.getContentResolver().insert(NoteEntry.buildAllNotesUri(), values);
        int id = (int) ContentUris.parseId(insertUri);

        // Create file for newly created database record
        File file = new File(Environment.getExternalStorageDirectory() + File.separator + fileName);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return id;
    }

    @Override
    protected void onPostExecute(Integer id) {

        try {
            ((OnAddListener) context).onItemAdded(id);
        } catch (ClassCastException e) {
            e.printStackTrace();
        }
    }


}
