package pl.tomaszmartin.stuff;

import android.content.Context;
import android.database.Cursor;

import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import pl.tomaszmartin.stuff.NotesContract.NoteEntry;

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

        long dateInMilliseconds = cursor.getLong(NoteEntry.NUMCOL_DATE_LAST_MODIFIED);
        viewHolder.dateView.setText(new SimpleDateFormat("dd MMMM").format(new Date(dateInMilliseconds)));
        String title = cursor.getString(NoteEntry.NUMCOL_TITLE);
        viewHolder.titleView.setText(title);
        String description = cursor.getString(NoteEntry.NUMCOL_DESCRIPTION);
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


    // TODO: this all goes to trash... hopefully...
    /*
    private Context context;
    private static NotesAdapter adapter;

    private NotesAdapter(Context context, ArrayList<Note> notes) {
        super(context, 0, notes);
        this.context = context;
    }

    public static NotesAdapter get(Context context, ArrayList<Note> notes) {
        return new NotesAdapter(context, notes);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Default list item view, not the one prepared
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.main_item, null);
        }


        Note note = getItem(position);
        TextView titleView = (TextView) convertView.findViewById(R.id.item_title);
        titleView.setText(note.getTitle());
        TextView subtitleView = (TextView) convertView.findViewById(R.id.item_desc);
        subtitleView.setText(note.getDescription());
        TextView dateView = (TextView) convertView.findViewById(R.id.date_edit);
        dateView.setText(calculateElapsedTime(note.getDate()));
        if (note.getImage() != null && !note.getImage().isEmpty()) {
            if (convertView.findViewById(R.id.image) != null) {
                ImageView imageView = (ImageView) convertView.findViewById(R.id.image);
                ImageTransformation imageTransformation = new ImageTransformation();
                Picasso
                        .with(context)
                        .load(Uri.parse(note.getImage()))
                        .transform(imageTransformation)
                        .into(imageView);
            }
        }

        return convertView;
    }

    private String calculateElapsedTime(Date date) {
        Date currentDate = new Date();
        String time;
        long diff = currentDate.getTime() - date.getTime();
        long diffMin = diff / (60 * 1000);
        long diffHours = diff / (60 * 60 * 1000);
        if (diffMin < 60l) {
            if (diffMin < 1l) {
                time = context.getString(R.string.less_than_min_ago);
            } else {
                time = (int) diffMin + " " + context.getString(R.string.min_ago);
            }
        } else if (diffHours < 24l) {
            if (diffHours < 2l) {
                time = context.getString(R.string.hour_ago);
            } else {
                time = (int) diffHours + " " + context.getString(R.string.hours_ago);
            }
        } else {
            DateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy");
            time = dateFormat.format(date);
        }

        return time;
    }

    public class ImageTransformation implements Transformation {

        @Override
        public Bitmap transform(Bitmap source) {
            DisplayMetrics metrics = new DisplayMetrics();
            WindowManager windowManager = (WindowManager) context
                    .getSystemService(Context.WINDOW_SERVICE);
            windowManager.getDefaultDisplay().getMetrics(metrics);
            float divider = 1.0f;
            if (source.getWidth() > metrics.widthPixels) {
                divider = source.getWidth() / metrics.widthPixels;
            }
            int x = Math.round(source.getWidth() / divider);
            int y = Math.round(source.getHeight() / divider);
            Bitmap result = Bitmap.createScaledBitmap(source, x, y, false);
            if (result != source) {
                source.recycle();
            }
            return result;
        }

        @Override
        public String key() {
            return "square()";
        }
    }
    */

}
