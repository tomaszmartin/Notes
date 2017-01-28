package pl.codeinprogress.notes.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;

import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.model.NotesRepository;
import pl.codeinprogress.notes.util.Encryption;
import pl.codeinprogress.notes.util.SchedulerProvider;
import pl.codeinprogress.notes.view.views.DetailsView;
import rx.functions.Action0;

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
                    e.printStackTrace();
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
            view.noteContentsLoaded(encryption.decrypt(content));
        });
    }

}
