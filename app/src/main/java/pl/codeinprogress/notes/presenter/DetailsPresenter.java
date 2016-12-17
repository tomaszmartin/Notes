package pl.codeinprogress.notes.presenter;

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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.view.BaseActivity;
import pl.codeinprogress.notes.view.views.DetailsView;
import pl.codeinprogress.notes.secret.Secrets;

public class DetailsPresenter {

    private DetailsView detailsView;
    private BaseActivity activity;
    private DatabaseReference database;
    private StorageReference storage;
    private String password;
    private File filesDir;

    public DetailsPresenter(DetailsView detailsView, BaseActivity activity) {
        this.detailsView = detailsView;
        this.activity = activity;
        this.database = FirebaseDatabase.getInstance().getReference(FirebaseLink.forNotes());
        this.storage = FirebaseStorage.getInstance().getReferenceFromUrl(Secrets.FIREBASE_STORAGE);
        this.password = activity.getAuthHandler().getCredentials().getId();
        this.filesDir = activity.getFilesDir();
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

    // todo: issues downloading files
    private void getNoteContent(Note note) {
        if (noteFileExists(note)) {
            loadNoteContentsFromFile(note);
        } else {
            downloadNoteFile(note);
        }
    }

    private void saveToFile(final Note note, final String password, final String content) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                if (!content.isEmpty() && !note.getFileName().isEmpty()) {
                    try {
                        Encryptor encryptor = new Encryptor(password);
                        String result = encryptor.encrypt(content);
                        FileOutputStream outputStream = new FileOutputStream(new File(filesDir, note.getFileName()));
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

    private void saveToFirebase( final Note note, final String password, final String content) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                try {
                    Encryptor encryptor = new Encryptor(password);
                    String encrypted = encryptor.encrypt(content);
                    InputStream stream = new ByteArrayInputStream(encrypted.getBytes());

                    FirebaseStorage storage = FirebaseStorage.getInstance();
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
        return new File(filesDir, note.getFileName()).exists();
    }

    private void loadNoteContentsFromFile(final Note note) {
        Runnable task = new Runnable() {
            @Override
            public void run() {
                String contents = "";
                final Encryptor encryptor = new Encryptor(password);
                try {
                    FileInputStream inputStream = new FileInputStream(new File(filesDir, note.getFileName()));
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
                            FileOutputStream outputStream = new FileOutputStream(new File(filesDir, note.getFileName()));
                            outputStream.write(bytes);
                            outputStream.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        displayContents(new String(bytes));
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(Exception e) {
                        displayContents("Error downloading file");
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
