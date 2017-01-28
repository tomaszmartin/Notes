package pl.codeinprogress.notes.view;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;
import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.presenter.DetailsPresenter;
import pl.codeinprogress.notes.util.NoteEditorView;
import pl.codeinprogress.notes.util.ImageTransformation;
import pl.codeinprogress.notes.view.views.DetailsView;

public class DetailsActivity extends BaseActivity implements DetailsView {

    public static String NOTE_ID = "noteId";
    private static final int IMAGE_REQUEST_CODE = 1;
    private static final int AUDIO_REQUEST_CODE = 2;
    private static final int CAMERA_REQUEST_CODE = 3;
    private TextToSpeech textToSpeech;
    private DetailsPresenter presenter;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        presenter = new DetailsPresenter(this, getRepository(), getScheduler());

        setupListeners();
        setupView();
        setupData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_details, menu);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            switch(requestCode) {
                case IMAGE_REQUEST_CODE:
                    setImage(data.getData());
                    break;
                case CAMERA_REQUEST_CODE:
                    break;
                case AUDIO_REQUEST_CODE:
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveNote();
    }

    @Override
    public void onDestroy() {
        if (textToSpeech != null) {
            textToSpeech.stop();
            textToSpeech.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch(id) {
            case R.id.action_share:
                shareNote();
                return true;
            case R.id.action_size:
                return true;
            case R.id.action_image:
                pickImage();
                return true;
            case R.id.action_camera:
                takePhoto();
                return true;
            case R.id.action_dictate:
                dictateNote();
                return true;
            case R.id.action_read:
                readNote();
                return true;
            case R.id.action_night_mode:
                switchNightMode();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showNote(Note note) {
        log(note.toString());
        this.note = note;

        EditText titleView = (EditText) findViewById(R.id.titleView);
        titleView.setText(note.getTitle());
    }

    @Override
    public void noteContentsLoaded(String contents) {
        NoteEditorView contentView = (NoteEditorView) findViewById(R.id.contentView);
        contentView.setContent(contents);
        contentView.setVisibility(View.VISIBLE);
    }




    private void setupView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
    }

    private void setupData() {
        String noteId = getIntent().getStringExtra(NOTE_ID);
        presenter.getNote(noteId);
    }

    private int getFontSize() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int size = 14;
        try {
            size =  Integer.valueOf(prefs.getString(getString(R.string.font_size_preference), getString(R.string.font_size_default)));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return size;
    }

    private void setFontSize(EditText view, int delta) {
        float currentSize = view.getTextSize();
        float defaultSize = 160f;
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dp = (currentSize / (displayMetrics.densityDpi / defaultSize)) + delta;

        view.setTextSize(TypedValue.COMPLEX_UNIT_SP, dp);
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), UUID.randomUUID().toString() + ".jpg"));
        if (intent.resolveActivity(getPackageManager()) != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
    }

    private void setupListeners() {
        textToSpeech = new TextToSpeech(this, status -> {
            Locale current = getResources().getConfiguration().locale;
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(current);
            }
        });

        NoteEditorView contentView = (NoteEditorView) findViewById(R.id.contentView);
        findViewById(R.id.listBulleted).setOnClickListener(view -> contentView.setBulletedList());
        findViewById(R.id.listNumbered).setOnClickListener(view -> contentView.setNumberedList());
        findViewById(R.id.formatBold).setOnClickListener(view -> contentView.toggleBold());
        findViewById(R.id.formatItalic).setOnClickListener(view -> contentView.toggleItalic());
    }

    private void pickImage() {
        Intent image = new Intent();
        image.setType("image/*");

        if (Build.VERSION.SDK_INT <= 19) {
            image.setAction(Intent.ACTION_GET_CONTENT);
        } else {
            image.setAction(Intent.ACTION_OPEN_DOCUMENT);
        }

        startActivityForResult(image, IMAGE_REQUEST_CODE);
    }

    private void saveNote() {
        if (null != note) {
            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            NoteEditorView contentView = (NoteEditorView) findViewById(R.id.contentView);
            EditText titleView = (EditText) findViewById(R.id.titleView);

            String content = contentView.getContent();
            String title = titleView.getText().toString();
            note.setTitle(title);
            note.setModified(new Date().getTime());
            note.setDescription(content);

            presenter.saveNote(note, content);
        }
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
            Toast.makeText(this.getApplicationContext(),
                    getString(R.string.dictating_not_supported),
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void readNote() {
        NoteEditorView contentView = (NoteEditorView) findViewById(R.id.contentView);

        String text = contentView.getContent();
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void shareNote() {
        EditText contentView = (EditText) findViewById(R.id.contentView);

        String content = contentView.getText().toString();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, content);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

    }

    private void setImage(Uri imageUri) {
        ImageView imageView = (ImageView) findViewById(R.id.imageView);

        imageView.setVisibility(View.VISIBLE);
        ImageTransformation imageTransformation = new ImageTransformation(this);

        if (imageUri != null && !imageUri.toString().isEmpty()) {
            Picasso.with(this)
                    .load(imageUri)
                    .transform(imageTransformation)
                    .into(imageView);

        }
    }

}