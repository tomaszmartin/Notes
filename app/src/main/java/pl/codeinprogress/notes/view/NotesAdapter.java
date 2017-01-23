package pl.codeinprogress.notes.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import io.realm.RealmBaseAdapter;
import io.realm.RealmResults;
import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.model.Note;

public class NotesAdapter extends RealmBaseAdapter<Note> implements ListAdapter {

    public NotesAdapter(Context context, RealmResults<Note> notes) {
        super(context, notes);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.main_item, null);
        }

        Note note = getItem(position);
        if (note != null) {
            TextView title = (TextView) view.findViewById(R.id.item_title);
            TextView date = (TextView) view.findViewById(R.id.item_date);
            TextView description = (TextView) view.findViewById(R.id.item_desc);

            DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(parent.getContext());
            title.setText(note.getTitle());
            date.setText(dateFormat.format(note.getModified()));
            description.setText(note.getDescription());

        }

        return view;

    }


}
