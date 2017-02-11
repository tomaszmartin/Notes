package pl.codeinprogress.notes.view;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;

import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.presenter.DetailsPresenter;
import pl.codeinprogress.notes.util.image.ImageTransformation;
import pl.codeinprogress.notes.view.views.DetailsView;

public class DetailsActivity extends BaseActivity implements DetailsView {

    public static final String NOTE_ID = "noteId";
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
        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setVisibility(View.VISIBLE);
        ImageTransformation imageTransformation = new ImageTransformation(this);

        if (imagePath != null && !imagePath.isEmpty()) {
            Picasso.with(this)
                    .load(imagePath)
                    .transform(imageTransformation)
                    .into(imageView);
        }
    }

    private void setupView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
        changeFontSize();
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

    private void saveNote() {
        if (null != note) {
            presenter.saveNote(getNote(), getNoteContent());
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

    private void changeFontSize() {
        try {
            int size =  Integer.valueOf(getPreferences().getString(getString(R.string.font_size_preference), getString(R.string.font_size_normal)));
            EditText editor = (EditText) findViewById(R.id.editor);
            editor.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        } catch (NumberFormatException e) {
            showErrorMessage(R.string.error_font);
        }
    }

}