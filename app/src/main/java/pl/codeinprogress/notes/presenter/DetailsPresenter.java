package pl.codeinprogress.notes.presenter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
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
import java.util.UUID;

import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.model.NotesRepository;
import pl.codeinprogress.notes.util.Encryption;
import pl.codeinprogress.notes.util.scheduler.SchedulerProvider;
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
        saveNoteContentsToFile(note, password, contents);
    }

    public void loadNote(@NonNull String noteId) {
        repository.getNote(noteId)
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .doOnError(error -> {
                    view.showErrorMessage(R.string.error_loading);
                })
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
                view.showErrorMessage(R.string.image_error);
            }
        });
    }

    void showNote(Note note) {
        view.showNote(note);
        loadNoteContentsFromFile(note);
    }

    void showNoteContents(final String content) {
        schedulerProvider.ui().createWorker().schedule(() -> {
            view.showNoteContents(getEncrypted(content));
        });
    }

    void saveNoteContentsToFile(final Note note, final String password, final String content) {
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
                        view.showErrorMessage(R.string.error_saving);
                    }
                }
            });
        }
    }

    void loadNoteContentsFromFile(final Note note) {
        if (note.getPath() != null && new File(note.getPath()).exists()) {
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
                    showNoteContents(contents);
                } catch (Exception e) {
                    view.showErrorMessage(R.string.error_loading);
                    showNoteContents("");
                }
            });
        } else {
            showNoteContents("");
        }
    }

    String getEncrypted(String message) {
        Encryption encryption = new Encryption(password);
        return encryption.decrypt(message);
    }

}
