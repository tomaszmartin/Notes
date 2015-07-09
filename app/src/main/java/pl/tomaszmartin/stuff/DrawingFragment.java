package pl.tomaszmartin.stuff;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import pl.tomaszmartin.stuff.NotesContract.NoteEntry;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.UUID;

/**
 * Created by tomaszmartin on 22.06.2015.
 */

public class DrawingFragment extends Fragment implements View.OnClickListener {

    private final String TAG = DrawingFragment.class.getSimpleName();
    private DrawingView drawingView;
    private Button saveButton;
    private ImageButton drawButton;
    private ImageButton eraseButton;
    private View rootView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.drawing_fragment, container, false);
        drawingView = (DrawingView) rootView.findViewById(R.id.drawing_view);
        saveButton = (Button) rootView.findViewById(R.id.save_button);
        saveButton.setOnClickListener(this);
        drawButton = (ImageButton) rootView.findViewById(R.id.draw_button);
        drawButton.setOnClickListener(this);
        eraseButton = (ImageButton) rootView.findViewById(R.id.erase_button);
        eraseButton.setOnClickListener(this);

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
        Bitmap image = drawingView.getDrawingCache();

        String filePath = saveImageToFile(image);
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
        } else if (id == R.id.draw_button) {
            drawingView.setDrawing();
        } else if (id == R.id.erase_button) {
            drawingView.setErasing();
        }
    }

    // Saving file locally
    private String saveImageToFile(Bitmap finalBitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File directory = new File(root + "/drawings/");
        directory.mkdirs();
        String fileName = UUID.randomUUID().toString()+".png";
        File file = new File (directory, fileName);
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 0, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Have to add file:// prefix, otherwise picasso doesn't recognize the path
        return "file://" + file.getAbsolutePath();
    }

    // Saving file to MediaStore
    // There are still problems on API 19
    private String saveImageToMediaStore(Bitmap image) {
        String imageName = UUID.randomUUID().toString()+".jpg";
        String filePath = MediaStore.Images.Media.insertImage(
                getActivity().getContentResolver(), image,
                imageName, "drawing");

        return filePath;
    }
}
