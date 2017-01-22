package pl.codeinprogress.notes.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.model.Note;

public class NotesAdapter extends ArrayAdapter<Note> {

    private NotesAdapter(Context context, int resource, List<Note> notes) {
        super(context, resource, notes);
    }

    public NotesAdapter(Context context, List<Note> notes) {
        this(context, -1, notes);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            view = inflater.inflate(R.layout.main_item, null);
        }

        Note note = getItem(position);
        if (note != null) {
            TextView title = (TextView) view.findViewById(R.id.item_title);
            TextView date = (TextView) view.findViewById(R.id.item_date);
            TextView description = (TextView) view.findViewById(R.id.item_desc);

            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getContext());
            title.setText(note.getTitle());
            date.setText(dateFormat.format(note.getLastModified()));
            description.setText(note.getDescription());

        }

        return view;

    }
}
