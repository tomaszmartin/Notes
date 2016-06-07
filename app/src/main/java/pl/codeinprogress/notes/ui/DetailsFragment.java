package pl.codeinprogress.notes.ui;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.Bind;
import butterknife.ButterKnife;
import pl.codeinprogress.notes.firebase.FirebaseActivity;
import pl.codeinprogress.notes.firebase.FirebaseFragment;
import pl.codeinprogress.notes.tasks.LoadNoteTask;
import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.tasks.SaveToDatabaseTask;
import pl.codeinprogress.notes.tasks.SaveToFileTask;
import pl.codeinprogress.notes.data.NotesContract.NoteEntry;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

/**
 * Created by tomaszmartin on 02.07.15.
 */

public class DetailsFragment extends FirebaseFragment implements LoaderManager.LoaderCallbacks<Cursor>, TextToSpeech.OnInitListener {

    private static final int DETAIL_LOADER = 1;
    private static final int IMAGE_REQUEST_CODE = 1;
    private static final int AUDIO_REQUEST_CODE = 2;
    private static final int CAMERA_REQUEST_CODE = 3;
    @Bind(R.id.content_view) EditText contentView;
    @Bind(R.id.image_view) ImageView imageView;
    private EditText titleView;
    private boolean hasResult = false;
    private Uri imageUri;
    private Cursor cursor;
    private Uri cameraPhotoUri;
    private TextToSpeech tts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.details_fragment, container, false);
        titleView = (EditText) getActivity().findViewById(R.id.title_view);
        ButterKnife.bind(this, rootView);

        tts = new TextToSpeech(getActivity(), this);

        contentView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getFontSize());
        registerForContextMenu(imageView);

        Bundle arguments = getArguments();
        if (arguments != null && arguments.containsKey(NoteEntry.COLUMN_ID)) {
            getLoaderManager().initLoader(DETAIL_LOADER, null, this);
        }

        return rootView;
    }

    public void setImage(Uri imageUri, ImageView imageView) {
        imageView.setVisibility(View.VISIBLE);
        ImageTransformation imageTransformation = new ImageTransformation();

        if (imageUri != null && !imageUri.toString().isEmpty()) {
            Picasso
                    .with(getActivity())
                    .load(imageUri)
                    .transform(imageTransformation)
                    .into(imageView);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && data != null) {
            if (requestCode == IMAGE_REQUEST_CODE || requestCode == CAMERA_REQUEST_CODE) {
                if (data.getStringExtra(NoteEntry.COLUMN_IMAGE_URI) != null && !data.getStringExtra(NoteEntry.COLUMN_IMAGE_URI).isEmpty()) {
                    imageUri = Uri.parse(data.getStringExtra(NoteEntry.COLUMN_IMAGE_URI));
                } else if (data.getData() != null) {
                    imageUri = data.getData();
                } else {
                    imageUri = cameraPhotoUri;
                }

                hasResult = true;
                setImage(imageUri, imageView);

            } else if (requestCode == AUDIO_REQUEST_CODE) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                contentView.setText(contentView.getText() + "\n" + result.get(0) + "\n");
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
        saveNote();
    }

    private void saveNote() {
        if (cursor != null && cursor.getCount() > 0) {
            SaveToFileTask saveToFileTask = new SaveToFileTask(this.getActivity(), getFileName());
            saveToFileTask.execute(getNoteContent());

            SaveToDatabaseTask saveToDatabaseTask = new SaveToDatabaseTask(this.getActivity(), getNoteUri());
            saveToDatabaseTask.execute(getContentValues());
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        setHasOptionsMenu(true);
    }

    public Uri getNoteUri() {
        return NoteEntry.buildNoteUri(cursor.getInt(cursor.getColumnIndex(NoteEntry.COLUMN_ID)));
    }

    public void removeImage() {
        if (imageView != null) {
            imageView.setVisibility(View.GONE);
            imageUri = Uri.parse("");
        }
    }

    public ContentValues getContentValues() {
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

    public String getNoteContent() {
        return contentView.getText().toString();
    }

    private String getDescription() {
        String desc = getNoteContent();

        desc = desc.replace("\n", " ");
        desc = desc.trim();

        return desc;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_share) {
            shareNote();
            return true;
        } else if (id == R.id.action_size) {
            setFontSize(contentView, 1);
            return true;
        } else if (id == R.id.action_alarm) {
            DialogFragment alertDialog = new AlarmDialogFragment();
            showDialog(alertDialog);
            return true;
        } else if (id == R.id.action_image) {
            pickImage();
            return true;
        } else if (id == R.id.action_camera) {
            takePhoto();
            return true;
        } else if (id == R.id.action_dictate) {
            dictateNote();
            return true;
        } else if (id == R.id.action_read) {
            readNote();
            return true;
        } else if (id == R.id.action_night_mode) {
            switchNightMode();
        }

        return super.onOptionsItemSelected(item);
    }

    private void switchNightMode() {
        if (getActivity() instanceof FirebaseActivity) {
            FirebaseActivity activity = (FirebaseActivity) getActivity();
            activity.switchNightMode();
        }
    }

    private void readNote() {
        String text = getNoteContent();
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void dictateNote() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speak_now));
        try {
            startActivityForResult(intent, AUDIO_REQUEST_CODE);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getActivity().getApplicationContext(),
                    getString(R.string.dicatating_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void shareNote() {
        String content = contentView.getText().toString();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, content);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);


        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(getNoteId()));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Note");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Note");
        logEvent(FirebaseAnalytics.Event.SHARE, bundle);
    }

    private void showDialog(DialogFragment dialogFragment) {
        Bundle args = new Bundle();
        args.putInt(NoteEntry.COLUMN_ID, getArguments().getInt(NoteEntry.COLUMN_ID));
        dialogFragment.setArguments(args);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        Fragment previousFragment = getFragmentManager().findFragmentByTag(dialogFragment.getClass().getSimpleName());
        if (previousFragment != null) {
            fragmentTransaction.remove(previousFragment);
        }
        fragmentTransaction.addToBackStack(null);

        dialogFragment.setStyle(DialogFragment.STYLE_NORMAL, R.style.dialogStyle);
        dialogFragment.show(getFragmentManager(), dialogFragment.getClass().getSimpleName());
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
        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
            String title = cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_TITLE));
            String fileName = cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_FILE_NAME));
            if (!hasResult && imageUri == null) {
                imageUri = Uri.parse(cursor.getString(cursor.getColumnIndex(NoteEntry.COLUMN_IMAGE_URI)));
            }
            if (!title.isEmpty()) {
                titleView.setText(title);
            }
            if (!fileName.isEmpty()) {
                LoadNoteTask loadTask = new LoadNoteTask(getActivity(), contentView);
                loadTask.execute(fileName);
            }
            if (!hasResult && !imageUri.toString().isEmpty()) {
                setImage(imageUri, imageView);
            }

        }
    }

    @Override
    public void onLoaderReset(Loader loader) { }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        getActivity().getMenuInflater().inflate(R.menu.image_menu, menu);
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_remove_image) {
            removeImage();
        } else if (id == R.id.action_change_image) {
            pickImage();
        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onInit(int status) {
        Locale current = getResources().getConfiguration().locale;
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(current);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            }

        } else {
            Log.e("TTS", "Initialization Failed!");
        }

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

    private void setFontSize(EditText view, int delta) {
        float currentSize = view.getTextSize();
        float defaultSize = 160f;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dp = (currentSize / (displayMetrics.densityDpi / defaultSize)) + delta;

        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, dp);
    }

    public int getFontSize() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        int size = 14;
        try {
            size =  Integer.valueOf(prefs.getString(getString(R.string.font_size_preference), getString(R.string.font_size_default)));
        } catch (NumberFormatException e) {

        }

        return size;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    public void takePhoto() {
        cameraPhotoUri = getPhotoUri();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        boolean canTakePhoto = cameraPhotoUri != null && intent.resolveActivity(getActivity().getPackageManager()) != null;
        if (canTakePhoto) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPhotoUri);
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }

    }

    public Uri getPhotoUri() {
        File externalFilesDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        if (externalFilesDir == null) {
            return null;
        }

        return Uri.fromFile(new File(externalFilesDir, "IMG_" + UUID.randomUUID().toString() + ".jpg"));
    }

    public int getNoteId() {
        return cursor.getInt(cursor.getColumnIndex(NoteEntry.COLUMN_ID));
    }

}