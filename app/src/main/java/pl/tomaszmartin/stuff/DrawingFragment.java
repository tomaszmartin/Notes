package pl.tomaszmartin.stuff;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import pl.tomaszmartin.stuff.NotesContract.NoteEntry;
import java.util.UUID;

/**
 * Created by tomaszmartin on 22.06.2015.
 */
public class DrawingFragment extends Fragment implements View.OnClickListener {

    private final String TAG = DrawingFragment.class.getSimpleName();
    private DrawingView drawingView;
    private Button saveButton;
    private View rootView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.drawing_fragment, container, false);
        drawingView = (DrawingView) rootView.findViewById(R.id.drawing_view);
        saveButton = (Button) rootView.findViewById(R.id.save_button);
        saveButton.setOnClickListener(this);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    private void saveImage() {
        // TODO: implement this method in background thread

        drawingView.setDrawingCacheEnabled(true);

        String imageName = UUID.randomUUID().toString()+".jpg";
        String filePath = MediaStore.Images.Media.insertImage(
                getActivity().getContentResolver(), drawingView.getDrawingCache(),
                imageName, "drawing");

        Bundle bundle = new Bundle();
        bundle.putString(NoteEntry.COLUMN_IMAGE_URI, filePath);
        Intent intent = new Intent();
        intent.putExtras(bundle);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();

        drawingView.destroyDrawingCache();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.save_button) {
            saveImage();
        }
    }
}
