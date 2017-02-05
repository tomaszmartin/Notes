package pl.codeinprogress.notes.model.local;

import android.provider.BaseColumns;

final class NotesContract implements BaseColumns {

    private NotesContract() {}

    static final String TABLE_NAME = "notes";
    static final String ENTRY_ID = "id";
    static final String TITLE = "title";
    static final String DESCRIPTION = "description";
    static final String PATH = "path";
    static final String CREATED = "created";
    static final String MODIFIED = "modified";
    static final String SECURED = "secured";

}
