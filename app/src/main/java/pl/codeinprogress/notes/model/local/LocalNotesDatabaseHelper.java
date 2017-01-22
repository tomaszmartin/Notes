package pl.codeinprogress.notes.model.local;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static pl.codeinprogress.notes.model.local.NotesContract.*;

public class LocalNotesDatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "notes.db";
    private final String SQL_CREATE = "CREATE TABLE " + TABLE_NAME + "("
            + _ID + " TEXT PRIMARY KEY"
            + ENTRY_ID + " TEXT ,"
            + TITLE + " TEXT, "
            + DESCRIPTION + " TEXT,"
            + PATH + " TEXT,"
            + CREATED + " DOUBLE,"
            + MODIFIED + " DOUBLE,"
            + SECURED + " INTEGER)";

    public LocalNotesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
