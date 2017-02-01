package pl.codeinprogress.notes.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;

import java.io.File;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import jp.wasabeef.richeditor.RichEditor;
import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.presenter.DetailsPresenter;
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
                    presenter.transformImage(data.getData(), this);
                    break;
                case CAMERA_REQUEST_CODE:
                    presenter.transformImage(data.getData(), this);
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
            case R.id.action_camera:
                takePhoto();
                return true;
            case R.id.action_read:
                readNote();
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
        RichEditor contentView = (RichEditor) findViewById(R.id.contentView);
        contentView.setHtml(contents);
    }

    @Override
    public void showErrorMessage(String message) {
        Snackbar.make(findViewById(R.id.coordinator), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void insertImage(String imagePath) {
        ((RichEditor) findViewById(R.id.contentView)).insertImage(imagePath, null);
    }




    private void setupView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }

        RichEditor editor = (RichEditor) findViewById(R.id.contentView);
        editor.setBackgroundColor(getResources().getColor(R.color.item_background));
        editor.setEditorFontColor(getResources().getColor(R.color.editor_text_color));
    }

    private void setupData() {
        String noteId = getIntent().getStringExtra(NOTE_ID);
        presenter.loadNote(noteId);
    }

    private void setupListeners() {
        textToSpeech = new TextToSpeech(this, status -> {
            Locale current = getResources().getConfiguration().locale;
            if (status == TextToSpeech.SUCCESS) {
                textToSpeech.setLanguage(current);
            }
        });

        RichEditor contentView = (RichEditor) findViewById(R.id.contentView);
        findViewById(R.id.listBulleted).setOnClickListener(view -> {
            contentView.setBullets();
            view.setSelected(!view.isSelected());
        });
        findViewById(R.id.formatBold).setOnClickListener(view -> {
            contentView.setBold();
            view.setSelected(!view.isSelected());
        });
        findViewById(R.id.formatItalic).setOnClickListener(view -> {
            contentView.setItalic();
            view.setSelected(!view.isSelected());
        });
        findViewById(R.id.formatUnderline).setOnClickListener(view -> {
            contentView.setUnderline();
            view.setSelected(!view.isSelected());
        });
        findViewById(R.id.formatImage).setOnClickListener(view -> {
            pickImage();
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
            presenter.saveNote(getNote(), getNoteHtml());
        }
    }

    private void readNote() {
        textToSpeech.speak(getNoteContent(), TextToSpeech.QUEUE_FLUSH, null);
    }

    private Note getNote() {
        note.setModified(new Date().getTime());
        note.setDescription(getNoteContent());

        return note;
    }

    private String getNoteContent() {
        String note = Html.fromHtml(getNoteHtml()).toString();
        return note;
    }

    private String getNoteHtml() {
        String content = ((RichEditor) findViewById(R.id.contentView)).getHtml();
        return content;
    }

    private void shareNote() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, getNoteContent());
        sendIntent.setType("text/plain");
        startActivity(sendIntent);

    }

}