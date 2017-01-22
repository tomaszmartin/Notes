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

    RealmNotesRepository() {
        realm = Realm.getDefaultInstance();
    }

    @Override
    public Note get(String noteId) {
        Note note = realm.where(Note.class).equalTo("id", noteId).findFirst();
        return note;
    }

    @Override
    public ArrayList<Note> getAll() {
        RealmResults<Note> results = realm.where(Note.class).findAll();
        ArrayList<Note> notes = new ArrayList<>(results);

        return notes;
    }

    @Override
    public void save(Note note) {
        realm.executeTransaction(realm -> realm.copyToRealmOrUpdate(note));
    }

    @Override
    public void delete(Note note) {
        note.deleteFromRealm();
    }

    @Override
    public String add() {
        final String noteId = UUID.randomUUID().toString();

        realm.executeTransaction(realm -> {
            Note note = realm.createObject(Note.class, noteId);
            long timestamp = new Date().getTime();
            note.setCreated(timestamp);
            note.setLastModified(timestamp);
        });

        return noteId;
    }

    @Override
    public ArrayList<Note> query(String query) {
        RealmResults<Note> results = realm.where(Note.class).contains("name", query, Case.INSENSITIVE).findAll();
        ArrayList<Note> notes = new ArrayList<>(results);

        return notes;
    }

}
