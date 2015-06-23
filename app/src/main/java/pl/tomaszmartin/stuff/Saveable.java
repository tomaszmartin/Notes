package pl.tomaszmartin.stuff;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;

/**
 * Created by tomaszmartin on 21.06.2015.
 */
public interface Saveable {

    public final int SAVE_DATABASE = 0;
    public final int SAVE_FILE = 1;
    public final int SAVE_DATABASE_AND_FILE = 2;

    public int getSaveType();
    public Context getContext();

    public String getContents();
    public String getFileName();

    public ContentValues getValues();
    public Uri getUri();

}
