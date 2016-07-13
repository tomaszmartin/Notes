package pl.codeinprogress.notes.presenter;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.model.data.firebase.FirebaseActivity;
import pl.codeinprogress.notes.model.data.firebase.FirebaseLink;
import pl.codeinprogress.notes.presenter.views.DetailsView;
import pl.codeinprogress.notes.secret.Secrets;

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
        if (noteFileExists(note)) {
            loadNoteContentsFromFile(note);
        } else {
            downloadNoteFile(note);
        }
    }

    private void saveToFile(@NonNull final Note note, final String password, @NonNull final String content) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                if (!content.isEmpty() && !note.getFileName().isEmpty()) {
                    try {
                        Encryptor encryptor = new Encryptor(password);
                        String result = encryptor.encrypt(content);
                        FileOutputStream outputStream = activity.openFileOutput(note.getFileName(), Context.MODE_PRIVATE);
                        outputStream.write(result.getBytes());
                        outputStream.close();
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

    private boolean noteFileExists(Note note) {
        return activity.getFileStreamPath(note.getFileName()).exists();
    }

    private void loadNoteContentsFromFile(final Note note) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                String contents = "";
                final Encryptor encryptor = new Encryptor(password);
                try {
                    FileInputStream inputStream = activity.openFileInput(note.getFileName());
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
            }
        };
        Thread thread = new Thread(task);
        thread.run();
    }

    private void downloadNoteFile(final Note note) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                StorageReference fileReference = storage.child(note.getFileName());
                final long oneMegabyte = 1024 * 1024;
                fileReference.getBytes(oneMegabyte).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                    @Override
                    public void onSuccess(final byte[] bytes) {
                        try {
                            FileOutputStream outputStream = activity.openFileOutput(note.getFileName(), Context.MODE_PRIVATE);
                            outputStream.write(bytes);
                            outputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        displayContents(new String(bytes));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        displayContents("");
                    }
                });
            }
        };
        Thread thread = new Thread(task);
        thread.run();
    }

    private void displayContents(final String content) {
        final Encryptor encryptor = new Encryptor(password);
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                detailsView.noteContentsLoaded(encryptor.decrypt(content));
            }
        });
    }

}
