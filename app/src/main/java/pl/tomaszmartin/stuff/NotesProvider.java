package pl.tomaszmartin.stuff;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

/**
 * Created by tomaszmartin on 14.06.2015.
 */
public class NotesProvider extends ContentProvider {

    private static final String TAG = NotesProvider.class.getSimpleName();
    private static final UriMatcher uriMatcher = buildUriMatcher();
    private NotesDatabaseHelper notesDatabaseHelper;

    private static final int NOTES = 100;
    private static final int NOTES_WITH_TYPE = 101;
    private static final int NOTES_WITH_TYPE_AND_CATEGORY = 102;
    private static final int NOTE = 103;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        // For getting list of note
        // # matches any number
        // * matches text
        matcher.addURI(NotesContract.CONTENT_AUTHORITY, NotesContract.PATH_NOTES, NOTES);

        // For getting a specific type of notes
        matcher.addURI(NotesContract.CONTENT_AUTHORITY, NotesContract.PATH_NOTES + "/#", NOTES_WITH_TYPE);

        // For getting a specific type of notes from a specific category
        matcher.addURI(NotesContract.CONTENT_AUTHORITY, NotesContract.PATH_NOTES + "/#/#", NOTES_WITH_TYPE_AND_CATEGORY);

        // For getting a single note
        matcher.addURI(NotesContract.CONTENT_AUTHORITY, NotesContract.PATH_NOTES + "/" + NotesContract.PATH_SINGLE_NOTE + "/#", NOTE);

        return matcher;
    }

    @Override
    public boolean onCreate() {
        notesDatabaseHelper = NotesDatabaseHelper.get(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = uriMatcher.match(uri);
        switch (match) {
            case NOTES:
                return NotesContract.NoteEntry.CONTENT_DIR_TYPE;
            case NOTES_WITH_TYPE:
                return NotesContract.NoteEntry.CONTENT_DIR_TYPE;
            case NOTES_WITH_TYPE_AND_CATEGORY:
                return NotesContract.NoteEntry.CONTENT_DIR_TYPE;
            case NOTE:
                return NotesContract.NoteEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Cursor cursor;
        switch (uriMatcher.match(uri)) {
            case NOTES:
                cursor = notesDatabaseHelper.getReadableDatabase().query(
                        NotesContract.NoteEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            case NOTES_WITH_TYPE:
                cursor = notesDatabaseHelper.getReadableDatabase().query(
                        NotesContract.NoteEntry.TABLE_NAME,
                        projection,
                        NotesContract.NoteEntry.COLUMN_TYPE + " = '" + uri.getPathSegments().get(1),
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            case NOTES_WITH_TYPE_AND_CATEGORY:
                cursor = notesDatabaseHelper.getReadableDatabase().query(
                        NotesContract.NoteEntry.TABLE_NAME,
                        projection,
                        NotesContract.NoteEntry.COLUMN_TYPE + " = '" + uri.getPathSegments().get(1) +
                        " AND " + NotesContract.NoteEntry.COLUMN_CATEGORY + uri.getPathSegments().get(2),
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            case NOTE:
                cursor = notesDatabaseHelper.getReadableDatabase().query(
                        NotesContract.NoteEntry.TABLE_NAME,
                        projection,
                        NotesContract.NoteEntry.COLUMN_ID + " = '" + ContentUris.parseId(uri) + "'",
                        null,
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final SQLiteDatabase db = notesDatabaseHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        Uri returnUri;

        switch (match) {
            case NOTES:
                long _id = db.insert(NotesContract.NoteEntry.TABLE_NAME, null, contentValues);
                // TODO: implement method to be sure that id will start from 1
                if (_id > 0) {
                    returnUri = NotesContract.NoteEntry.buildNoteUri(_id);
                } else {
                    throw new SQLException("Failed to insert data into " + uri);
                }
                break;
            default:
                throw new UnsupportedOperationException("Unknown Uri: " + uri);
        }

        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = notesDatabaseHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsDeleted;

        switch (match) {
            case NOTE:
                rowsDeleted = db.delete(
                        NotesContract.NoteEntry.TABLE_NAME,
                        NotesContract.NoteEntry.COLUMN_ID + " = '" + ContentUris.parseId(uri) + "'",
                        selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        // null deletes all rows
        if (selection == null || rowsDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final SQLiteDatabase db = notesDatabaseHelper.getWritableDatabase();
        final int match = uriMatcher.match(uri);
        int rowsUpdated;
        if (selection == null || selection.isEmpty()) {
            selection = NotesContract.NoteEntry.COLUMN_ID + " = '" + ContentUris.parseId(uri) + "'";
        }

        switch (match) {
            case NOTE:
                rowsUpdated = db.update(NotesContract.NoteEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowsUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowsUpdated;
    }

}
