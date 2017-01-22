package pl.codeinprogress.notes.model;

import android.support.annotation.NonNull;

import java.util.List;

import io.reactivex.Observable;
import pl.codeinprogress.notes.model.Note;

public interface NotesDataSource {

    Observable<List<Note>> getNotes();
    Observable<Note> getNote(@NonNull String noteId);
    void saveNote(@NonNull Note note);
    void deleteTask(@NonNull Note note);

}
