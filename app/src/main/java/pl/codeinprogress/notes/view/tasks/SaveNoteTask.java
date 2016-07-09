package pl.codeinprogress.notes.view.tasks;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.presenter.Encryptor;
import pl.codeinprogress.notes.model.data.firebase.FirebaseActivity;

/**
 * Created by tomaszmartin on 21.06.2015.
 */

public class SaveNoteTask extends AsyncTask<String, Void, Void> {

    private Context context;
    private String noteFilePath;
    private String password;

    public SaveNoteTask(Context context, String noteFilePath) {
        this.context = context;
        this.noteFilePath = noteFilePath;
        if (this.context instanceof FirebaseActivity) {
            password = ((FirebaseActivity) context).getAuthHandler().getCredentials().getId();
        } else {
            password = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        }
    }

    @Override
    protected Void doInBackground(String... contents) {
        String content = contents[0];
        saveToFile(content);
        saveToFirebase(content);

        return null;
    }

    private void saveToFile(String content) {
        if (!content.isEmpty() && !noteFilePath.isEmpty()) {
            try {
                Encryptor encryptor = new Encryptor(password);
                content = encryptor.encrypt(content);
                FileOutputStream fos = context.openFileOutput(noteFilePath, Activity.MODE_PRIVATE);
                fos.write(content.getBytes());
                fos.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    private void saveToFirebase(String content) {
        if (context instanceof FirebaseActivity) {
            try {
                FirebaseActivity activity = (FirebaseActivity) context;
                Encryptor encryptor = new Encryptor(password);
                String encrypted = encryptor.encrypt(content);
                InputStream stream = new ByteArrayInputStream(encrypted.getBytes());

                FirebaseStorage storage = activity.getStorage();
                StorageReference reference = storage.getReferenceFromUrl(context.getString(R.string.firebase_storage_bucket));
                StorageReference current = reference.child(noteFilePath);

                current.putStream(stream);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
