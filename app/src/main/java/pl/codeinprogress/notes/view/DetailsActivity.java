package pl.codeinprogress.notes.view;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.io.File;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.presenter.DetailsPresenter;
import pl.codeinprogress.notes.view.views.DetailsView;

public class DetailsActivity extends BaseActivity implements DetailsView {

    public static final String NOTE_ID = "noteId";
    public static final int IMAGE_REQUEST_CODE = 1;
    public static final int AUDIO_REQUEST_CODE = 2;
    public static final int CAMERA_REQUEST_CODE = 3;
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
            switch (requestCode) {
                case IMAGE_REQUEST_CODE:
                    presenter.transformImage(data.getData(), this);
                    break;
                case CAMERA_REQUEST_CODE:
                    presenter.transformImage(data.getData(), this);
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
        switch (id) {
            case R.id.action_share:
                shareNote();
                return true;
            case R.id.action_camera:
                takePhoto();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showNote(Note note) {
        this.note = note;
    }

    @Override
    public void showNoteContents(String contents) {
        EditText editor = (EditText) findViewById(R.id.editor);
        editor.setText(contents);
    }

    @Override
    public void showErrorMessage(int messageId) {
        Snackbar.make(findViewById(R.id.coordinator), getString(messageId), Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void insertImage(String imagePath) {
        // Todo: add image capabilities
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
        presenter.loadNote(noteId);
    }

    @SuppressWarnings("deprecation")
    private void setupListeners() {
        textToSpeech = new TextToSpeech(this, status -> {
            Locale current = null;
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
                current = getResources().getConfiguration().getLocales().get(0);
            } else {
                current = getResources().getConfiguration().locale;
            }
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(current);
            }
        });
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        Uri photoUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), UUID.randomUUID().toString() + ".jpg"));
        if (intent.resolveActivity(getPackageManager()) != null) {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            startActivityForResult(intent, CAMERA_REQUEST_CODE);
        }
    }

    private void saveNote() {
        if (null != note) {
            presenter.saveNote(getNote(), getNoteContent());
        }
    }

    @SuppressWarnings("deprecation")
    private void readNote() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            textToSpeech.speak(getNoteContent(), TextToSpeech.QUEUE_FLUSH, null, null);

        } else {
            textToSpeech.speak(getNoteContent(), TextToSpeech.QUEUE_FLUSH, null);
        }
    }

    private Note getNote() {
        note.setModified(new Date().getTime());
        note.setDescription(getNoteContent());

        return note;
    }

    private String getNoteContent() {
        return ((EditText) findViewById(R.id.editor)).getText().toString();
    }

    private void shareNote() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getNoteContent());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

    }

    @SuppressWarnings("unused")
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

    @SuppressWarnings("unused")
    private void dictate() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                getString(R.string.speak_now));
        try {
            startActivityForResult(intent, AUDIO_REQUEST_CODE);
        } catch (ActivityNotFoundException a) {
            showErrorMessage(R.string.dictating_not_supported);
        }
    }

}