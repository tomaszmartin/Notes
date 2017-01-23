package pl.codeinprogress.notes.model.local;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import java.util.List;

import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.model.NotesDataSource;
import pl.codeinprogress.notes.util.SchedulerProvider;
import rx.Observable;
import rx.functions.Func1;
import static pl.codeinprogress.notes.model.local.NotesContract.*;
import static com.google.common.base.Preconditions.checkNotNull;

public class LocalNotesDataSource implements NotesDataSource {

    @Nullable
    private static LocalNotesDataSource instance;
    @NonNull
    private static BriteDatabase database;

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
        String[] projection = {
                ENTRY_ID,
                TITLE,
                DESCRIPTION,
                PATH,
                CREATED,
                MODIFIED,
                SECURED
        };
        String sql = String.format("SELECT %s FROM %s", TextUtils.join(",", projection), TABLE_NAME);
        return database.createQuery(TABLE_NAME, sql).mapToList(this::getNote);
    }

    @Override
    public Observable<Note> getNote(@NonNull String noteId) {
        String[] projection = {
                ENTRY_ID,
                TITLE,
                DESCRIPTION,
                PATH,
                CREATED,
                MODIFIED,
                SECURED
        };
        String sql = String.format("SELECT %s FROM %s WHERE %s LIKE ?", TextUtils.join(",", projection), TABLE_NAME, ENTRY_ID);
        return database.createQuery(TABLE_NAME, sql, noteId).mapToOneOrDefault(this::getNote, null);
    }

    @Override
    public void saveNote(@NonNull Note note) {
        checkNotNull(note);
        ContentValues values = new ContentValues();
        values.put(ENTRY_ID, note.getId());
        values.put(TITLE, note.getTitle());
        values.put(DESCRIPTION, note.getDescription());
        values.put(PATH, note.getPath());
        values.put(CREATED, note.getCreated());
        values.put(MODIFIED, note.getModified());
        values.put(SECURED, note.isSecured());

        database.insert(TABLE_NAME, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    @Override
    public void deleteNote(@NonNull String noteId) {
        String selection = ENTRY_ID + "LIKE ?";
        String[] selectionArgs = {noteId};

        database.delete(TABLE_NAME, selection, selectionArgs);
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