package pl.tomaszmartin.stuff.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import pl.tomaszmartin.stuff.R;

/**
 * Created by tomaszmartin on 16.08.2015.
 */

public class TagDialogFragment extends DialogFragment implements AdapterView.OnItemClickListener, DialogInterface.OnClickListener {

    private ArrayList<String> tags;
    private final String TAG = TagDialogFragment.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tags = new ArrayList<>();
        tags.add("private");
        tags.add("work");
        tags.add("programming");
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Create builder and view for dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View rootView = getActivity().getLayoutInflater().inflate(R.layout.dialog_tag, null);

        // Set up listView
        ListView listView = (ListView) rootView.findViewById(R.id.taglist);
        listView.setAdapter(new TagsAdapter(getActivity(), R.layout.tag_item, tags));
        listView.setItemsCanFocus(true);
        listView.setOnItemClickListener(this);

        // Set up edit text
        EditText editText = (EditText) rootView.findViewById(R.id.newtag);
        editText.setImeActionLabel(getString(R.string.action_add), KeyEvent.KEYCODE_ENTER);

        // Set up buttons and listeners
        builder.setNegativeButton(R.string.cancel, this);
        builder.setPositiveButton(R.string.ok, this);

        builder.setView(rootView);
        return builder.create();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkbox);
        checkBox.setChecked(!checkBox.isChecked());
    }

    @Override
    public void onClick(DialogInterface dialog, int which) {
        if (which == DialogInterface.BUTTON_POSITIVE) {

        } else if (which == DialogInterface.BUTTON_NEGATIVE) {
            
        }
    }

    private class TagsAdapter extends ArrayAdapter<String> {

        ArrayList<String> tags;

        public TagsAdapter(Context context, int resource, ArrayList<String> objects) {
            super(context, resource, objects);
            this.tags = objects;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                LayoutInflater inflater = LayoutInflater.from(getActivity());
                convertView = inflater.inflate(R.layout.tag_item, null);
                holder = new ViewHolder();
                holder.text = (TextView) convertView.findViewById(R.id.name);
                holder.checkbox = (CheckBox) convertView.findViewById(R.id.checkbox);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            String name = tags.get(position);
            if (name.indexOf("#") != 0) {
                name = "#" + name;
            }

            holder.text.setText(name);
            return convertView;
        }

        private class ViewHolder {
            TextView text;
            CheckBox checkbox;
        }
    }

}