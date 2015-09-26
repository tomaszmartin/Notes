package pl.tomaszmartin.stuff;

import android.content.Context;
import android.database.Cursor;

import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import pl.tomaszmartin.stuff.data.NotesContract.NoteEntry;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by tomaszmartin on 14.06.2015.
 */

public class NotesAdapter extends CursorAdapter {

    private static final String TAG = NotesAdapter.class.getSimpleName();

    public NotesAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = LayoutInflater.from(context).inflate(R.layout.main_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();

        long dateInMilliseconds = cursor.getLong(cursor.getColumnIndex(NoteEntry.COLUMN_DATE_CREATED));
        viewHolder.dateView.setText(new SimpleDateFormat("dd MMMM").format(new Date(dateInMilliseconds)));
        String title = cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_TITLE));
        viewHolder.titleView.setText(title);
        String description = cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_DESCRIPTION));
        viewHolder.descriptionView.setText(description);
    }

    // Class for reusing views in list view
    public static class ViewHolder {
        public final TextView titleView;
        public final TextView descriptionView;
        public final TextView dateView;

        public ViewHolder(View view) {
            titleView = (TextView) view.findViewById(R.id.item_title);
            descriptionView = (TextView) view.findViewById(R.id.item_desc);
            dateView = (TextView) view.findViewById(R.id.item_date);
        }
    }

}
