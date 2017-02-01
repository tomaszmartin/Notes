package pl.codeinprogress.notes.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import rx.Observable;

public class NotesRepository implements NotesDataSource {

    @Nullable
    private static NotesRepository instance = null;
    @NonNull
    private NotesDataSource dataSource;

    private NotesRepository(@NonNull NotesDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public static NotesRepository getInstance(@NonNull NotesDataSource dataSource) {
        if (instance == null) {
            instance = new NotesRepository(dataSource);
        }

        return instance;
    }

    @Override
    public Observable<List<Note>> getNotes() {
        return dataSource.getNotes();
    }

    @Override
    public Observable<Note> getNote(@NonNull String noteId) {
        if (null != noteId && noteId.isEmpty()) {
            return dataSource.getNote(noteId);
        } else {
            return null;
        }

    }

    @Override
    public void saveNote(@NonNull Note note) {
        dataSource.saveNote(note);
    }

    @Override
    public void deleteNote(@NonNull String noteId) {
        dataSource.deleteNote(noteId);
    }

    @Override
    public void addNote(@NonNull String noteId) {
        dataSource.addNote(noteId);
    }

    @NonNull
    public NotesDataSource getDataSource() {
        return dataSource;
    }

}