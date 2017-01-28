package pl.codeinprogress.notes.view.views;

import pl.codeinprogress.notes.model.Note;

public interface DetailsView {

    void showNote(Note note);
    void showNoteContents(String htmlContents);
    void showErrorMessage(String message);
    void insertImage(String imagePath);

}
