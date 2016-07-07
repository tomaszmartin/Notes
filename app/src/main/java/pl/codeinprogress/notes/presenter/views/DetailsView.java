package pl.codeinprogress.notes.presenter.views;

import pl.codeinprogress.notes.model.Note;

/**
 * Created by tomaszmartin on 05.07.2016.
 */

public interface DetailsView {

    void noteLoaded(Note note);
    void noteContentsLoaded(String contents);

}
