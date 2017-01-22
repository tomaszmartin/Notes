package pl.codeinprogress.notes.model.local;

import android.provider.BaseColumns;

public final class NotesContract implements BaseColumns {

    private NotesContract() {}

    public static final String TABLE_NAME = "notes";
    public static final String ENTRY_ID = "id";
    public static final String TITLE = "title";
    public static final String DESCRIPTION = "description";
    public static final String PATH = "path";
    public static final String CREATED = "created";
    public static final String MODIFIED = "modified";
    public static final String SECURED = "secured";

}
