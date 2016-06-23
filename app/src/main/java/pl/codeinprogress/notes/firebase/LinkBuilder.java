package pl.codeinprogress.notes.firebase;

import pl.codeinprogress.notes.data.Note;
import pl.codeinprogress.notes.model.User;

/**
 * Created by tomaszmartin on 23.06.2016.
 */

public class LinkBuilder {

    public static String forUsers() {
        return "/users";
    }

    public static String forUser(User user) {
        return forUsers() + "/" + user.getId();
    }

    public static String forNotes() {
        return "/notes";
    }

    public static String forNote(Note note) {
        return forNotes() + "/" + note.getId();
    }

}
