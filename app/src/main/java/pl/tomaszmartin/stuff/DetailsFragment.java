package pl.tomaszmartin.stuff;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import pl.tomaszmartin.stuff.NotesContract.NoteEntry;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.Date;

public class DetailsFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>, Saveable {

    private final String TAG = DetailsFragment.class.getSimpleName();
    private static final int DETAIL_LOADER = 1;
    private static final int IMAGE_REQUEST_CODE = 1;
    private static final int AUDIO_REQUEST_CODE = 2;
    private EditText textView;
    private EditText titleView;
    private View rootView;
    private ImageView imageView;
    private boolean hasResult = false;
    private Uri imageUri;
    private Cursor cursor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.details_fragment, container, false);

        // get a handle of all the necessary views
        textView = (EditText) rootView.findViewById(R.id.text_edit);
        imageView = (ImageView) rootView.findViewById(R.id.image);
        titleView = (EditText) rootView.findViewById(R.id.title_edit);
        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(NoteEntry.COLUMN_ID)) {
            getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        }

        return rootView;
    }

    private void setImage(Uri imageUri, ImageView imageView) {

        //if (imageView.getVisibility() == View.GONE) {
            imageView.setVisibility(View.VISIBLE);
        //}
        ImageTransformation imageTransformation = new ImageTransformation();

        if (imageUri != null && !imageUri.toString().isEmpty()) {
            Log.d(TAG, "file path as uri is " + imageUri.toString());
            Picasso
                    .with(getActivity())
                    .load(imageUri)
                    .transform(imageTransformation)
                    .into(imageView);
        }
    }

    // TODO: after clicking back button on DrawingFragment the image is no longer changing nor saving until stopping fragment
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == IMAGE_REQUEST_CODE) {
                if (data.getStringExtra(NoteEntry.COLUMN_IMAGE_URI) != null && !data.getStringExtra(NoteEntry.COLUMN_IMAGE_URI).isEmpty()) {
                    // For images saved in the provider
                    imageUri = Uri.parse(data.getStringExtra(NoteEntry.COLUMN_IMAGE_URI));
                } else {
                    imageUri = data.getData();
                }

                // Has to set this flag to true so onLoadFinished won't load previous image
                hasResult = true;
                setImage(imageUri, imageView);

            } else if (requestCode == AUDIO_REQUEST_CODE) {

            }
        }
    }

    public void pickImage() {
        Intent image = new Intent();
        image.setType("image/*");

        if (Build.VERSION.SDK_INT <= 19) {
            image.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            image.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }

        startActivityForResult(image, IMAGE_REQUEST_CODE);
    }


    @Override
    public void onPause() {
        super.onPause();
        if (cursor != null) {
            SaveTask saveTask = new SaveTask();
            saveTask.execute(this);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public int getSaveType() {
        return Saveable.SAVE_DATABASE_AND_FILE;
    }

    public Context getContext() {
        return getActivity();
    }

    public Uri getUri() {
        return NoteEntry.buildNoteUri(cursor.getInt(cursor.getColumnIndex(NoteEntry.COLUMN_ID)));
    }

    public ContentValues getValues() {
        if (imageUri == null) {
            imageUri = Uri.parse("");
        }
        ContentValues values = new ContentValues();

        values.put(NoteEntry.COLUMN_ID, cursor.getInt(cursor.getColumnIndex(NoteEntry.COLUMN_ID)));
        values.put(NoteEntry.COLUMN_TITLE, titleView.getText().toString());
        values.put(NoteEntry.COLUMN_DESCRIPTION, getDescription());
        values.put(NoteEntry.COLUMN_FILE_NAME, cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_FILE_NAME)));
        values.put(NoteEntry.COLUMN_TYPE, cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_TYPE)));
        values.put(NoteEntry.COLUMN_DATE_CREATED, cursor.getLong(cursor.getColumnIndex(NoteEntry.COLUMN_DATE_CREATED)));
        values.put(NoteEntry.COLUMN_DATE_LAST_MODIFIED, new Date().getTime());
        values.put(NoteEntry.COLUMN_CATEGORY, cursor.getInt(cursor.getColumnIndex(NoteEntry.COLUMN_CATEGORY)));
        values.put(NoteEntry.COLUMN_IMAGE_URI, imageUri.toString());

        return values;
    }

    public String getFileName() {
        return cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_FILE_NAME));
    }

    public String getContents() {
        return textView.getText().toString();
    }

    private String getDescription() {
        String desc = textView.getText().toString();
        int maxCharInDescription = 320;
        if (desc.length() > maxCharInDescription) {
            String start = desc.substring(0, maxCharInDescription);
            String stop = desc.substring(maxCharInDescription);
            if (stop.indexOf(" ") < 10) {
                stop = stop.substring(0, stop.indexOf(" "));
                desc = start + stop;
            }
        }

        desc = desc.replace("\n", " ");
        desc = desc.trim();

        return desc;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {

            return true;
        } else if (id == R.id.action_send) {
            EditText contentView = (EditText) getActivity().findViewById(R.id.text_edit);
            String content = contentView.getText().toString();
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, content);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);

            return true;
        } else if (id == R.id.action_size) {

            return true;
        } else if (id == R.id.action_alarm) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());
            View view = getActivity().getLayoutInflater().inflate(R.layout.datetime_picker, null);
            final TimePicker timePicker = (TimePicker) view.findViewById(R.id.time);
            final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date);
            dialog.setView(view);
            dialog.show();

            return true;
        } else if (id == R.id.action_image) {
            pickImage();

            return true;
        } else if (id == R.id.action_draw) {
            Intent intent = new Intent(getActivity(), DrawingActivity.class);
            intent.putExtra(NoteEntry.COLUMN_ID, cursor.getInt(cursor.getColumnIndex(NoteEntry.COLUMN_ID)));
            startActivityForResult(intent, IMAGE_REQUEST_CODE);

            return true;
        } else if (id == R.id.action_remove_image) {
            ImageView imageView = (ImageView) getActivity().findViewById(R.id.image);
            imageView.setVisibility(View.GONE);
            imageUri = Uri.parse("");

            return true;
        } else if (id == R.id.action_record) {
            Intent intent = new Intent(getActivity(), RecordActivity.class);
            intent.putExtra(NoteEntry.COLUMN_ID, cursor.getInt(cursor.getColumnIndex(NoteEntry.COLUMN_ID)));
            startActivityForResult(intent, AUDIO_REQUEST_CODE);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        int noteId = getArguments().getInt(NoteEntry.COLUMN_ID);
        Uri noteUri = NoteEntry.buildNoteUri(noteId);

        return new CursorLoader(
                getActivity(),
                noteUri,
                NoteEntry.NOTE_COLUMNS,
                null,
                null,
                null
        );
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cur) {
        cursor = cur;
        if (cursor != null && cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_TITLE));
            String fileName = cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_FILE_NAME));
            if (!hasResult && imageUri == null) {
                imageUri = Uri.parse(cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_IMAGE_URI)));
            }
            if (!title.isEmpty()) {
                titleView.setText(title);
            }
            if (!fileName.isEmpty()) {
                LoadNoteTask loadTask = new LoadNoteTask(getActivity(), textView);
                loadTask.execute(fileName);
            }
            if (!hasResult && !imageUri.toString().isEmpty()) {
                setImage(imageUri, imageView);
            }

        }
    }

    @Override
    public void onLoaderReset(Loader loader) {

    }

    public class ImageTransformation implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {
            DisplayMetrics metrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
            float divider = 1.0f;
            // 100 pixels for padding
            int padding = 100;
            int minWidth = metrics.widthPixels - padding;
            if (source.getWidth() > minWidth) {
                divider = source.getWidth() / minWidth;
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

}