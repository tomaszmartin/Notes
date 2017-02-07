package pl.codeinprogress.notes.view.views;

import java.util.List;

import pl.codeinprogress.notes.model.Note;

public interface NotesView {

    void showNotes(List<Note> notes);
    void showErrorMessage(int messageId);
    void openEditView(String noteId);

}
