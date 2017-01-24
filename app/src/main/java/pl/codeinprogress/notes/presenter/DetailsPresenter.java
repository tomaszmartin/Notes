package pl.codeinprogress.notes.presenter;

import android.support.annotation.NonNull;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.model.NotesRepository;
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
        saveToFile(note, password, contents);

    }

    public void getNote(String noteId) {
        repository.getNote(noteId)
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .subscribe(note -> {
                    view.showNote(note);
                });
    }


    private void showNote(Note note) {
        view.showNote(note);
        // getNoteContent(note);
    }

    private void getNoteContent(Note note) {
        if (noteFileExists(note)) {
            loadNoteContentsFromFile(note);
        }
    }

    private void saveToFile(final Note note, final String password, final String content) {
        Runnable task = () -> {
            if (!content.isEmpty() && !note.getPath().isEmpty()) {
                try {
                    Encryptor encryptor = new Encryptor(password);
                    String result = encryptor.encrypt(content);
                    FileOutputStream outputStream = new FileOutputStream(new File(note.getPath()));
                    outputStream.write(result.getBytes());
                    outputStream.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(task);
        thread.run();
    }

    private boolean noteFileExists(Note note) {
        //return new File(filesDir, note.getPath()).exists();
        return false;
    }

    private void loadNoteContentsFromFile(final Note note) {
        Runnable task = () -> {
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
        };
        Thread thread = new Thread(task);
        thread.run();
    }

    private void displayContents(final String content) {
        final Encryptor encryptor = new Encryptor(password);
        // activity.runOnUiThread(() -> view.noteContentsLoaded(encryptor.decrypt(content)));
    }

}
