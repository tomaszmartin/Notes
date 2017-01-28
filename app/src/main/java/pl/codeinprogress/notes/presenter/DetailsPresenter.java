package pl.codeinprogress.notes.presenter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.model.NotesRepository;
import pl.codeinprogress.notes.util.Encryption;
import pl.codeinprogress.notes.util.ImageTransformation;
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

    public void getNote(String noteId) {
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

    private void getNoteContent(Note note) {
        if (noteFileExists(note)) {
            loadNoteContentsFromFile(note);
        } else {
            displayContents("");
        }
    }

    private void saveToFile(final Note note, @NonNull final String password, @NonNull final String content) {
        schedulerProvider.io().createWorker().schedule(() -> {
            if (!content.isEmpty() && !note.getPath().isEmpty()) {
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

    private boolean noteFileExists(Note note) {
        return new File(note.getPath()).exists();
    }

    private void loadNoteContentsFromFile(final Note note) {
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

    private void displayContents(final String content) {
        final Encryption encryption = new Encryption(password);
        schedulerProvider.ui().createWorker().schedule(() -> {
            view.showNoteContents(encryption.decrypt(content));
        });
    }

}
