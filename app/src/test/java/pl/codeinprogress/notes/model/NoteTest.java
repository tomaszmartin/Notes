package pl.codeinprogress.notes.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NoteTest {

    @Test
    public void shouldReturnUserId() {
        String userid = "userID";
        Note note = new Note(userid);
        assertEquals(userid, note.getId());
    }

    @Test
    public void shouldCreateShorterDescription() {
        String userid = "userID";
        Note note = new Note(userid);
        String tested = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n"
                + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n"
                + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n"
                + "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n";
        String expected = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n" +
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.\n" +
                "Lorem";
        note.setDescription(tested);
        assertEquals(expected, note.getDescription());
    }

}