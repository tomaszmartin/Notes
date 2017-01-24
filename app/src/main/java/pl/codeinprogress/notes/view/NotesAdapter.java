package pl.codeinprogress.notes.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
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

public class NotesAdapter extends BaseAdapter {

    private List<Note> notes;

    public NotesAdapter(List<Note> notes) {
        this.notes = notes;
    }

    public void setData(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Object getItem(int position) {
        return notes.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            view = inflater.inflate(R.layout.main_item, null);
        }

        Note note = (Note) getItem(position);
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
