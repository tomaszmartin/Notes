package pl.codeinprogress.notes.presenter;

import android.app.Activity;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.model.NotesRepository;
import pl.codeinprogress.notes.view.BaseActivity;
import pl.codeinprogress.notes.view.views.DetailsView;

public class DetailsPresenter {

    private DetailsView view;
    private NotesRepository repository;
    private Activity activity;
    private String password;
    private File filesDir;

    // todo: create repository
    public DetailsPresenter(DetailsView view, BaseActivity activity) {
        this.view = view;
        // todo: change password
        this.password = "todo";
        this.activity = activity;
        this.filesDir = activity.getFilesDir();
    }

    public void saveNote(Note note, String contents) {
        if (repository != null) {
            // todo
            //repository.save(note);
            saveToFile(note, password, contents);
        }
    }

    public void getNote(String noteId) {
        if (repository != null) {
            // todo
            //Note note = repository.getNote(noteId);
            // showNote(note);
        }
    }




    private void showNote(Note note) {
        view.noteLoaded(note);
        getNoteContent(note);
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
                    FileOutputStream outputStream = new FileOutputStream(new File(filesDir, note.getPath()));
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
        return new File(filesDir, note.getPath()).exists();
    }

    private void loadNoteContentsFromFile(final Note note) {
        Runnable task = () -> {
            String contents = "";
            try {
                FileInputStream inputStream = new FileInputStream(new File(filesDir, note.getPath()));
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
        activity.runOnUiThread(() -> view.noteContentsLoaded(encryptor.decrypt(content)));
    }

}
