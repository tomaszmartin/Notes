package pl.codeinprogress.notes.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import pl.codeinprogress.notes.data.NotesContract.NoteEntry;

/**
 * Created by tomaszmartin on 26.03.15.
 * This class is a main database of the app.
 * It stores all the information about the notes.
 */

public class NotesDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "stuff.db";
    public static final String TAG = NotesDatabaseHelper.class.getSimpleName();

    private static NotesDatabaseHelper db;

    private NotesDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Database is a singleton
    public static NotesDatabaseHelper get(Context context) {
        if (db == null) {
            db = new NotesDatabaseHelper(context);
        }

        return db;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate() called");
        final String SQL_CREATE_NOTES_TABLE = "CREATE TABLE IF NOT EXISTS " + NoteEntry.TABLE_NAME + "(" +
                NoteEntry.COLUMN_ID + " INTEGER PRIMARY KEY, " +
                NoteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                NoteEntry.COLUMN_DESCRIPTION + " TEXT, " +
                NoteEntry.COLUMN_FILE_NAME + " TEXT NOT NULL, " +
                NoteEntry.COLUMN_TYPE + " INTEGER NOT NULL, " +
                NoteEntry.COLUMN_DATE_CREATED + " REAL NOT NULL, " +
                NoteEntry.COLUMN_DATE_LAST_MODIFIED + " REAL NOT NULL, " +
                NoteEntry.COLUMN_CATEGORY + " INTEGER NOT NULL, " +
                NoteEntry.COLUMN_IMAGE_URI + " TEXT);";

        db.execSQL(SQL_CREATE_NOTES_TABLE);
        Log.d(TAG, "Database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: create onUpgrade() method for NotesDatabase
    }

}