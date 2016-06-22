package pl.codeinprogress.notes.firebase;

import pl.codeinprogress.notes.data.Note;
import pl.codeinprogress.notes.model.User;

/**
 * Created by tomaszmartin on 23.06.2016.
 */

public class FirebaseLinkBuilder {

    private static FirebaseLinkBuilder instance;
    private FirebaseApplication application;

    private FirebaseLinkBuilder(FirebaseApplication application) {
        this.application = application;
    }

    private FirebaseLinkBuilder getInstance(FirebaseApplication application) {
        if (instance == null) {
            instance = new FirebaseLinkBuilder(application);
        }

        return instance;
    }

    public String forUsers() {
        return "users";
    }

    public String forUser(User user) {
        return new StringBuilder().append(forUsers()).append("/").append(user.getId()).toString();
    }

    public String forNotes() {
        return "notes";
    }

    public String forNote(Note note) {
        return new StringBuilder().append(forNotes()).append("/").append(note.getId()).toString();
    }

}
