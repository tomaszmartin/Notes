package pl.codeinprogress.notes.model;

import android.support.annotation.NonNull;

import java.util.List;

import rx.Observable;


public interface NotesDataSource {

    Observable<List<Note>> getNotes();
    Observable<Note> getNote(@NonNull String noteId);
    Observable<List<Note>> queryNotes(@NonNull String query);
    void saveNote(@NonNull Note note);
    void deleteNote(@NonNull String noteId);
    void addNote(@NonNull String noteId);

}
