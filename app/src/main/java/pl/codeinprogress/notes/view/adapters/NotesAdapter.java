package pl.codeinprogress.notes.view.adapters;

import android.support.annotation.NonNull;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.List;

import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.model.Note;

public class NotesAdapter extends BaseAdapter {

    private List<Note> notes;

    public NotesAdapter(List<Note> notes) {
        this.notes = notes;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    @Override
    public Note getItem(int position) {
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

        Note note = getItem(position);
        if (note != null && note.getDescription() != null) {
            TextView description = (TextView) view.findViewById(R.id.item_desc);
            description.setText(Html.fromHtml(note.getDescription()));
        }
        if (note != null) {
            DateFormat dateFormat = DateFormat.getDateInstance();
            TextView date = (TextView) view.findViewById(R.id.item_date);
            date.setText(dateFormat.format(note.getModified()));
        }

        return view;

    }





    public void setData(List<Note> notes) {
        this.notes = notes;
        notifyDataSetChanged();
    }

}
