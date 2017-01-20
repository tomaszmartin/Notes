package pl.codeinprogress.notes.presenter;

import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

import io.realm.Case;
import io.realm.Realm;
import io.realm.RealmResults;
import pl.codeinprogress.notes.model.Note;

public class RealmNotesRepository implements NotesRepository {

    private Realm realm;

    public RealmNotesRepository() {
        realm = Realm.getDefaultInstance();
    }

    @Override
    public Note getNote(String noteId) {
        return realm.where(Note.class).equalTo("id", noteId).findFirst();
    }

    @Override
    public ArrayList<Note> getNotes() {
        RealmResults<Note> results = realm.where(Note.class).findAll();
        ArrayList<Note> notes = new ArrayList<>(results);

        return notes;
    }

    @Override
    public void saveNote(Note note) {
        realm.executeTransaction(realm -> realm.copyToRealmOrUpdate(note));
    }

    @Override
    public void deleteNote(Note note) {
        note.deleteFromRealm();
    }

    @Override
    public String addNote() {
        final String noteId = UUID.randomUUID().toString();

        realm.executeTransaction(realm -> {
            Note note = realm.createObject(Note.class);
            long timestamp = new Date().getTime();
            note.setCreated(timestamp);
            note.setLastModified(timestamp);
            note.setId(noteId);
        });

        return noteId;
    }

    @Override
    public ArrayList<Note> searchNotes(String query) {
        RealmResults<Note> results = realm.where(Note.class).contains("name", query, Case.INSENSITIVE).findAll();
        ArrayList<Note> notes = new ArrayList<>(results);

        return notes;
    }
}
