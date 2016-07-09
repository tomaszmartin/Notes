package pl.codeinprogress.notes.view.tasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.StorageReference;
import com.google.repacked.antlr.runtime.debug.RemoteDebugEventSocketListener;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import pl.codeinprogress.notes.presenter.Encryptor;
import pl.codeinprogress.notes.model.data.firebase.FirebaseActivity;
import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.presenter.views.DetailsView;

/**
 * Created by tomaszmartin on 21.06.2015.
 */

public class LoadNoteTask extends AsyncTask<Note, Void, String> {

    private Activity context;
    private DetailsView view;
    private String password;
    private Note note;
    private StorageReference storage;

    public LoadNoteTask(Activity context, DetailsView view, StorageReference storage) {
        this.context = context;
        this.view = view;
        if (this.context instanceof FirebaseActivity) {
            password = ((FirebaseActivity) context).getAuthHandler().getCredentials().getId();
        } else {
            password = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
        this.storage = storage;
    }

    @Override
    protected String doInBackground(Note... notes) {
        this.note = notes[0];
        String contents = "";
        final Encryptor encryptor = new Encryptor(password);
        if (context.getFileStreamPath(note.getFileName()).exists()) {
            try {
                FileInputStream fis = context.openFileInput(note.getFileName());
                BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(fis)));
                String line;
                while ((line = reader.readLine()) != null) {
                    contents = contents + line;
                }
                fis.close();
                return encryptor.decrypt(contents);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            StorageReference fileReference = storage.child(note.getFileName());
            final long oneMegabyte = 1024 * 1024;
            fileReference.getBytes(oneMegabyte).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                @Override
                public void onSuccess(final byte[] bytes) {
                    try {
                        FileOutputStream fos = context.openFileOutput(note.getFileName(), Activity.MODE_PRIVATE);
                        fos.write(bytes);
                        fos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            view.noteContentsLoaded(encryptor.decrypt(new String(bytes)));
                        }
                    });
                }
            });
        }


        return contents;
    }

    @Override
    protected void onPostExecute(String contents) {
        view.noteContentsLoaded(contents);
    }

}
