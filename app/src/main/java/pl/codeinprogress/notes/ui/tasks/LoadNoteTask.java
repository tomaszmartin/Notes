package pl.codeinprogress.notes.ui.tasks;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import pl.codeinprogress.notes.data.EncryptionHelper;
import pl.codeinprogress.notes.firebase.FirebaseApplication;

/**
 * Created by tomaszmartin on 21.06.2015.
 */

public class LoadNoteTask extends AsyncTask<String, Void, String> {

    private Context context;
    private TextView view;
    private String password;

    public LoadNoteTask(Context ctx, TextView view) {
        this.context = ctx.getApplicationContext();
        this.view = view;
        if (this.context instanceof FirebaseApplication) {
            password = ((FirebaseApplication) context).getAuthHandler().getCredentials().getId();
        } else {
            password = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
    }

    @Override
    protected String doInBackground(String... strings) {
        String fileName = strings[0];
        String contents = "";

        try {
            FileInputStream fis = context.openFileInput(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(fis)));
            String line;
            while ((line = reader.readLine()) != null) {
                contents = contents + line;
            }
            log("Before EncryptionHelper " + contents);
            EncryptionHelper encryptionHelper = new EncryptionHelper(password);
            contents = encryptionHelper.decrypt(contents);

            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return contents;
    }

    @Override
    protected void onPostExecute(String contents) {
        view.setText(contents);
        view.setVisibility(View.VISIBLE);
    }

    private void log(String message) {
        Log.d(LoadNoteTask.class.getSimpleName(), message);
    }

}
