package pl.tomaszmartin.stuff;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import pl.tomaszmartin.stuff.NotesContract.NoteEntry;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by tomaszmartin on 26.03.15.
 * This class is a main database of the app.
 * It stores all the information about the notes.
 *
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

    public ArrayList<Note> getData() {

        // List of notes
        ArrayList<Note> notesList = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        final String SQL_SELECT = "SELECT " +
                NoteEntry.COLUMN_ID + ", " +
                NoteEntry.COLUMN_TITLE + ", " +
                NoteEntry.COLUMN_DESCRIPTION + ", " +
                NoteEntry.COLUMN_FILE_NAME + ", " +
                NoteEntry.COLUMN_TYPE + ", " +
                NoteEntry.COLUMN_DATE_CREATED + ", " +
                NoteEntry.COLUMN_DATE_LAST_MODIFIED + ", " +
                NoteEntry.COLUMN_CATEGORY + ", " +
                NoteEntry.COLUMN_IMAGE_URI + " " +
                " FROM " + NoteEntry.TABLE_NAME +
                " ORDER BY " + NoteEntry.COLUMN_DATE_LAST_MODIFIED;

        Cursor cursor = db.rawQuery(SQL_SELECT, null);
        while (cursor.moveToNext()) {

            // Retrieve all the values from database
            int id = cursor.getInt(0);
            String title = cursor.getString(1);
            String description = cursor.getString(2);
            String fileName = cursor.getString(3);
            int type = cursor.getInt(4);
            long dateCreated = cursor.getLong(5);
            long dateLastModified = cursor.getLong(6);
            int category = cursor.getInt(7);
            String imageUri = cursor.getString(8);

            // Create a note with the current values
            Note note = new Note(id, title, fileName, type);
            note.setDate(new Date(dateCreated));
            note.setLastModified(new Date(dateLastModified));
            note.setDescription(description);
            note.setCategory(category);
            note.setImage(imageUri);

            notesList.add(note);
        }

        db.close();
        Log.d(TAG, "Data was successfully selected");

        return notesList;
    }

    public void updateData(Note note) {

        SQLiteDatabase db = getWritableDatabase();

        String SQL_WHERE = "WHERE " + NoteEntry.COLUMN_ID + " = " + note.getId();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteEntry.COLUMN_TITLE, note.getTitle());
        contentValues.put(NoteEntry.COLUMN_DESCRIPTION, note. getDescription());
        contentValues.put(NoteEntry.COLUMN_FILE_NAME, note.getFileName());
        contentValues.put(NoteEntry.COLUMN_TYPE, note.getType());
        contentValues.put(NoteEntry.COLUMN_DATE_CREATED, note.getDate().getTime());
        contentValues.put(NoteEntry.COLUMN_DATE_LAST_MODIFIED, note.getLastModified().getTime());
        contentValues.put(NoteEntry.COLUMN_CATEGORY, note.getCategory());
        contentValues.put(NoteEntry.COLUMN_IMAGE_URI, note.getImage());

        try {
            db.update(NoteEntry.TABLE_NAME, contentValues, SQL_WHERE, null);
        } catch (Exception e) {
            String error =  e.getMessage();
            Log.d(TAG, error);
        }
        db.close();

        Log.d(TAG, "Data was successfully updated");
    }

    public void deleteData(Note note) {

        SQLiteDatabase db = getWritableDatabase();

        String SQL_WHERE = "WHERE " + NoteEntry.COLUMN_ID + " = " + note.getId();
        try {
            db.delete(NoteEntry.TABLE_NAME, SQL_WHERE, null);
        } catch (Exception e) {
            String error =  e.getMessage();
            Log.d(TAG, error);
        }
        db.close();

        Log.d(TAG, "Data was successfully deleted");
    }

    public void insertData(Note note) {

        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(NoteEntry.COLUMN_ID, note.getId());
        contentValues.put(NoteEntry.COLUMN_TITLE, note.getTitle());
        contentValues.put(NoteEntry.COLUMN_DESCRIPTION, note. getDescription());
        contentValues.put(NoteEntry.COLUMN_FILE_NAME, note.getFileName());
        contentValues.put(NoteEntry.COLUMN_TYPE, note.getType());
        contentValues.put(NoteEntry.COLUMN_DATE_CREATED, note.getDate().getTime());
        contentValues.put(NoteEntry.COLUMN_DATE_LAST_MODIFIED, note.getLastModified().getTime());
        contentValues.put(NoteEntry.COLUMN_CATEGORY, note.getCategory());
        contentValues.put(NoteEntry.COLUMN_IMAGE_URI, note.getImage());

        try {
            db.insert(NoteEntry.TABLE_NAME, "undefined", contentValues);
        } catch (Exception e) {
            Log.d(TAG, e.toString());
        }
        db.close();

        Log.d(TAG, "Data was successfully inserted");
    }

}