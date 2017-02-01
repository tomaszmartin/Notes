package pl.codeinprogress.notes.model;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class NoteTest {

    @Test
    public void shouldCreateShorterDescription() {
        Note note = new Note();
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

    @Test
    public void shouldCreateNullDescription() {
        Note note = new Note();
        String tested = null;
        note.setDescription(tested);
        assertNull(note.getDescription());
    }

    @Test
    public void sameNotesShouldBeEqual() {
        Note note = new Note();
        Note tested = new Note();
        assertEquals(note, tested);
    }

}