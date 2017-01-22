package pl.codeinprogress.notes.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.reactivex.Observable;

public class NotesRepository implements NotesDataSource {

    @Nullable
    private static NotesRepository instance = null;
    @NonNull
    private final NotesDataSource dataSource;

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
        return null;
    }

    @Override
    public Observable<Note> getNote(@NonNull String noteId) {
        return null;
    }

    @Override
    public void saveNote(@NonNull Note note) {

    }

    @Override
    public void deleteTask(@NonNull Note note) {

    }
}
