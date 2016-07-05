package pl.codeinprogress.notes.ui.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import pl.codeinprogress.notes.data.EncryptionHelper;
import pl.codeinprogress.notes.data.firebase.FirebaseActivity;
import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.ui.views.DetailsView;
import pl.codeinprogress.notes.ui.views.MainView;

/**
 * Created by tomaszmartin on 21.06.2015.
 */

public class LoadNoteTask extends AsyncTask<Note, Void, String> {

    private Context context;
    private DetailsView view;
    private String password;
    private Note note;

    public LoadNoteTask(Context context, DetailsView view) {
        this.context = context;
        this.view = view;
        if (this.context instanceof FirebaseActivity) {
            password = ((FirebaseActivity) context).getAuthHandler().getCredentials().getId();
        } else {
            password = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
    }

    @Override
    protected String doInBackground(Note... notes) {
        this.note = notes[0];
        String contents = "";
        try {
            FileInputStream fis = context.openFileInput(note.getFileName());
            BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(fis)));
            String line;
            while ((line = reader.readLine()) != null) {
                contents = contents + line;
            }
            EncryptionHelper encryptionHelper = new EncryptionHelper(password);
            fis.close();
            return encryptionHelper.decrypt(contents);
        } catch (FileNotFoundException e) {
            // TODO: handle downloading files on first use
        } catch (Exception e) {
            e.printStackTrace();
        }

        return contents;
    }

    @Override
    protected void onPostExecute(String contents) {
        view.viewNoteContent(contents);
    }

}
