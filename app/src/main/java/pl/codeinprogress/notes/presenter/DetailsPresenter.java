package pl.codeinprogress.notes.presenter;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.model.data.firebase.FirebaseActivity;
import pl.codeinprogress.notes.model.data.firebase.FirebaseLink;
import pl.codeinprogress.notes.presenter.views.DetailsView;
import pl.codeinprogress.notes.secret.Secrets;
import pl.codeinprogress.notes.view.tasks.LoadNoteTask;

/**
 * Created by tomaszmartin on 05.07.2016.
 */

public class DetailsPresenter {

    private DetailsView detailsView;
    private FirebaseActivity activity;
    private DatabaseReference database;
    private StorageReference storage;
    private String password;

    public DetailsPresenter(DetailsView detailsView, FirebaseActivity activity) {
        this.detailsView = detailsView;
        this.activity = activity;
        this.database = FirebaseDatabase.getInstance().getReference(FirebaseLink.forNotes());
        this.storage = FirebaseStorage.getInstance().getReferenceFromUrl(Secrets.FIREBASE_STORAGE);
        this.password = activity.getAuthHandler().getCredentials().getId();
    }

    public void saveNote(Note note, String contents) {
        database.child(note.getId()).setValue(note);
        saveToFile(note, password, contents);
        saveToFirebase(note, password, contents);
    }

    public void getNote(String noteId) {
        DatabaseReference noteReference = database.child(noteId);
        noteReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Note note = dataSnapshot.getValue(Note.class);
                if (detailsView != null) {
                    detailsView.noteLoaded(note);
                    getNoteContent(note);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void getNoteContent(Note note) {
        LoadNoteTask loadNoteTask = new LoadNoteTask(activity, detailsView, storage);
        loadNoteTask.execute(note);
    }

    private void saveToFile(@NonNull final Note note, final String password, @NonNull final String content) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                if (!content.isEmpty() && !note.getFileName().isEmpty()) {
                    try {
                        Encryptor encryptor = new Encryptor(password);
                        String result = encryptor.encrypt(content);
                        FileOutputStream fos = activity.openFileOutput(note.getFileName(), Activity.MODE_PRIVATE);
                        //FileOutputStream fos = new FileOutputStream(new File(note.getFileName()));
                        fos.write(result.getBytes());
                        fos.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        Thread thread = new Thread(task);
        thread.run();
    }

    private void saveToFirebase(@NonNull final Note note, final String password, @NonNull final String content) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    Encryptor encryptor = new Encryptor(password);
                    String encrypted = encryptor.encrypt(content);
                    InputStream stream = new ByteArrayInputStream(encrypted.getBytes());

                    FirebaseStorage storage = activity.getStorage();
                    StorageReference reference = storage.getReferenceFromUrl(Secrets.FIREBASE_STORAGE);
                    StorageReference current = reference.child(note.getFileName());
                    current.putStream(stream);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        Thread thread = new Thread(task);
        thread.run();
    }

}
