package pl.codeinprogress.notes.firebase;

import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.data.Note;
import pl.codeinprogress.notes.model.User;

/**
 * Created by tomaszmartin on 23.06.2016.
 */

public class FirebaseLinkBuilder {

    private static FirebaseLinkBuilder instance;
    private String main;

    private FirebaseLinkBuilder(FirebaseApplication application) {
        this.main = application.getString(R.string.firebase_database_url);
    }

    private FirebaseLinkBuilder getInstance(FirebaseApplication application) {
        if (instance == null) {
            instance = new FirebaseLinkBuilder(application);
        }

        return instance;
    }

    public String forUsers() {
        return main + "users";
    }

    public String forUser(User user) {
        return forUsers() + "/" + user.getId();
    }

    public String forNotes() {
        return main + "notes";
    }

    public String forNote(Note note) {
        return forNotes() + "/" + note.getId();
    }

}
