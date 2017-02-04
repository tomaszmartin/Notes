package pl.codeinprogress.notes.presenter;

import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.model.NotesRepository;
import pl.codeinprogress.notes.util.Encryption;
import pl.codeinprogress.notes.util.SchedulerProvider;
import pl.codeinprogress.notes.view.views.DetailsView;

public class DetailsPresenter {

    @NonNull
    private DetailsView view;
    @NonNull
    private NotesRepository repository;
    @NonNull
    private SchedulerProvider schedulerProvider;
    private String password = "secret";

    public DetailsPresenter(@NonNull DetailsView view, @NonNull NotesRepository repository, @NonNull SchedulerProvider provider) {
        this.view = view;
        this.repository = repository;
        this.schedulerProvider = provider;
    }

    public void saveNote(Note note, String contents) {
        repository.saveNote(note);
        saveToFile(note, password, contents);
    }

    public void loadNote(@NonNull String noteId) {
        Log.d(DetailsPresenter.class.getSimpleName(), "loadNote: called with id" + noteId);
        repository.getNote(noteId)
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe(this::showNote);
    }

    public void transformImage(Uri imageUri, AppCompatActivity context) {
        schedulerProvider.io().createWorker().schedule(() -> {
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), imageUri);

                DisplayMetrics metrics = new DisplayMetrics();
                context.getWindowManager().getDefaultDisplay().getMetrics(metrics);
                float scalingFactor = metrics.widthPixels / (bitmap.getWidth() * 3f);
                int width = Math.round(bitmap.getWidth() * scalingFactor);
                int height = Math.round(bitmap.getHeight() * scalingFactor);
                Log.d(DetailsPresenter.class.getSimpleName(), "transformImage: " + width + ", " + height);

                Bitmap scaledBitmap = Bitmap.createScaledBitmap(bitmap, width, height, true);
                File scaledImage = new File(context.getFilesDir(), "scaled" + imageUri.getLastPathSegment());
                FileOutputStream outputStream = new FileOutputStream(scaledImage);
                scaledBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();
                schedulerProvider.ui().createWorker().schedule(() -> view.insertImage(scaledImage.getPath()));
            } catch (IOException exception) {
                view.showErrorMessage("Error while retrieving image!");
            }
        });
    }





    private void showNote(Note note) {
        view.showNote(note);
        getNoteContent(note);
    }

    void getNoteContent(Note note) {
        if (noteFileExists(note)) {
            loadNoteContentsFromFile(note);
        } else {
            displayContents("");
        }
    }

    private void saveToFile(final Note note, final String password, final String content) {
        if (note != null && content != null) {
            schedulerProvider.io().createWorker().schedule(() -> {
                if (!content.isEmpty() && note.getPath() != null && !note.getPath().isEmpty()) {
                    try {
                        Encryption encryption = new Encryption(password);
                        String result = encryption.encrypt(content);
                        FileOutputStream outputStream = new FileOutputStream(new File(note.getPath()));
                        outputStream.write(result.getBytes());
                        outputStream.close();
                    } catch (Exception e) {
                        view.showErrorMessage("Error while saving note!");
                    }
                }
            });
        }
    }

    private boolean noteFileExists(Note note) {
        return note.getPath() != null && new File(note.getPath()).exists();

    }

    private void loadNoteContentsFromFile(final Note note) {
        if (note.getPath() != null) {
            schedulerProvider.io().createWorker().schedule(() -> {
                String contents = "";
                try {
                    FileInputStream inputStream = new FileInputStream(new File(note.getPath()));
                    BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(inputStream)));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        contents = contents + line;
                    }
                    inputStream.close();
                    displayContents(contents);
                } catch (Exception e) {
                    e.printStackTrace();
                    displayContents("");
                }
            });
        }
    }

    void displayContents(final String content) {
        schedulerProvider.ui().createWorker().schedule(() -> {
            view.showNoteContents(getEncrypted(content));
        });
    }

    String getEncrypted(String message) {
        Encryption encryption = new Encryption(password);
        return encryption.decrypt(message);
    }

}
