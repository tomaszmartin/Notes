package pl.codeinprogress.notes.ui.adapters;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;
import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.DatabaseReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.model.Note;

/**
 * Created by tomaszmartin on 22.06.2016.
 */

public class NotesAdapter extends FirebaseListAdapter<Note> {

    private Locale locale;

    public NotesAdapter(Activity activity, Class<Note> modelClass, int modelLayout, DatabaseReference ref) {
        super(activity, modelClass, modelLayout, ref);
        this.locale = activity.getResources().getConfiguration().locale;
    }

    @Override
    protected void populateView(View view, Note model, int position) {
        TextView titleView = (TextView) view.findViewById(R.id.item_title);
        TextView descriptionView = (TextView) view.findViewById(R.id.item_desc);
        TextView dateView = (TextView) view.findViewById(R.id.item_date);

        titleView.setText(model.getTitle());
        descriptionView.setText(model.getDescription());
        dateView.setText(new SimpleDateFormat("dd MMMM", locale).format(new Date(model.getCreated())));
    }

}
