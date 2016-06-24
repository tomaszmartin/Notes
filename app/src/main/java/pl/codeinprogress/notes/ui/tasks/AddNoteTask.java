package pl.codeinprogress.notes.ui.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import pl.codeinprogress.notes.model.Note;
import java.io.File;
import java.io.IOException;

/**
 * Created by tomaszmartin on 15.06.2015.
 */

public class AddNoteTask extends AsyncTask<Note, Void, String> {

    private final Context context;

    public AddNoteTask(Context context) {
        this.context = context;
    }

    @Override
    protected String doInBackground(Note... notes) {
        try {
            Note note = notes[0];
            File file = new File(Environment.getExternalStorageDirectory() + File.separator + note.getFileName());
            file.createNewFile();
            return note.getFileName();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String noteId) {
    }


}
