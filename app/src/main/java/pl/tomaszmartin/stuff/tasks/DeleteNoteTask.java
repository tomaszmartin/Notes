package pl.tomaszmartin.stuff.tasks;

import android.content.Context;
import android.os.AsyncTask;

import pl.tomaszmartin.stuff.data.NotesContract;

/**
 * Created by tomaszmartin on 18.06.2015.
 */
public class DeleteNoteTask extends AsyncTask<Integer, Void, Void> {

    private Context context;

    public DeleteNoteTask(Context context) {
        this.context = context;
    }

    @Override
    protected Void doInBackground(Integer... integers) {
        long id = (long) integers[0];

        context.getContentResolver().delete(NotesContract.NoteEntry.buildNoteUri(id), null, null);

        return null;
    }

}
