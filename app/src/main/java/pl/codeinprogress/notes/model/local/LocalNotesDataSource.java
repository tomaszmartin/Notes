package pl.codeinprogress.notes.model.local;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.BoolRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import io.reactivex.Observable;
import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.model.NotesDataSource;
import pl.codeinprogress.notes.util.SchedulerProvider;
import rx.functions.Func1;
import static pl.codeinprogress.notes.model.local.NotesContract.*;

public class LocalNotesDataSource implements NotesDataSource {

    @Nullable
    private static LocalNotesDataSource instance;
    @NonNull
    private static BriteDatabase database;
    @NonNull
    private Func1<Cursor, Note> mapper;

    private LocalNotesDataSource(@NonNull Context context, @NonNull SchedulerProvider schedulerProvider) {
        LocalNotesDatabaseHelper databaseHelper = new LocalNotesDatabaseHelper(context);
        SqlBrite sqlBrite = new SqlBrite.Builder().build();
        database = sqlBrite.wrapDatabaseHelper(databaseHelper, schedulerProvider.io());
    }

    public static LocalNotesDataSource getInstance(@NonNull Context context, @NonNull SchedulerProvider schedulerProvider) {
        if (instance == null) {
            instance = new LocalNotesDataSource(context, schedulerProvider);
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



    @NonNull
    private Note getNote(@NonNull Cursor cursor) {
        Note note = new Note();
        note.setId(cursor.getString(cursor.getColumnIndexOrThrow(ENTRY_ID)));
        note.setTitle(cursor.getString(cursor.getColumnIndexOrThrow(TITLE)));
        note.setDescription(cursor.getString(cursor.getColumnIndexOrThrow(DESCRIPTION)));
        note.setPath(cursor.getString(cursor.getColumnIndexOrThrow(PATH)));
        note.setCreated(cursor.getLong(cursor.getColumnIndexOrThrow(CREATED)));
        note.setModified(cursor.getLong(cursor.getColumnIndexOrThrow(MODIFIED)));
        note.setSecured(cursor.getInt(cursor.getColumnIndexOrThrow(SECURED)) == 1);

        return note;
    }

}
