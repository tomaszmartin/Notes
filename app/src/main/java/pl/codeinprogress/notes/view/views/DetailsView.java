package pl.codeinprogress.notes.view.views;

import pl.codeinprogress.notes.model.Note;

public interface DetailsView {

    void noteLoaded(Note note);
    void noteContentsLoaded(String contents);

}
