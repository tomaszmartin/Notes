package pl.codeinprogress.notes.model.local;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.sqlbrite.BriteDatabase;
import com.squareup.sqlbrite.SqlBrite;

import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.util.SchedulerProvider;
import rx.functions.Func1;

public class LocalNotesDataSource {

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

}
