package pl.codeinprogress.notes.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.speech.tts.TextToSpeech;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.Locale;
import java.util.UUID;
import butterknife.Bind;
import butterknife.ButterKnife;
import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.firebase.FirebaseActivity;
import pl.codeinprogress.notes.firebase.LinkBuilder;
import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.ui.image.ImageTransformation;
import pl.codeinprogress.notes.ui.listeners.NotesListener;


public class DetailsActivity extends FirebaseActivity {

    private static final int IMAGE_REQUEST_CODE = 1;
    private static final int AUDIO_REQUEST_CODE = 2;
    private static final int CAMERA_REQUEST_CODE = 3;
    public static String NOTE_ID = "noteId";
    @Bind(R.id.contentView) EditText contentView;
    @Bind(R.id.imageView) ImageView imageView;
    @Bind(R.id.titleView) EditText titleView;
    private DatabaseReference noteReference;
    private TextToSpeech textToSpeech;
    private Note note;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);
        ButterKnife.bind(this);

        setupView();
        setupListeners();
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
                setFontSize(contentView, 1);
                return true;
            case R.id.action_alarm:
                showAlarm();
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

    private void showAlarm() {
        DialogFragment alertDialog = new AlarmDialogFragment();
        alertDialog.show(getSupportFragmentManager(), DetailsActivity.class.getSimpleName());
    }

    private void setupView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("");
        }
        contentView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, getFontSize());
    }

    private int getFontSize() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        int size = 14;
        try {
            size =  Integer.valueOf(prefs.getString(getString(R.string.font_size_preference), getString(R.string.font_size_default)));
        } catch (NumberFormatException e) {

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
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int status) {
                Locale current = getResources().getConfiguration().locale;
                if (status == TextToSpeech.SUCCESS) {
                    textToSpeech.setLanguage(current);
                }
            }

        });
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

    }

    private void setupData() {
        String noteId = getIntent().getStringExtra(NOTE_ID);
        noteReference = getDatabase().getReference(LinkBuilder.forNote(noteId));
        noteReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                note = dataSnapshot.getValue(Note.class);
                showNote(note);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void showNote(Note note) {
        titleView.setText(note.getTitle());
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
        String text = contentView.getText().toString();
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }

    private void shareNote() {
        String content = contentView.getText().toString();
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, content);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);


        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(note.getId()));
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Note");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "Note");
        logEvent(FirebaseAnalytics.Event.SHARE, bundle);
    }

    public void setImage(Uri imageUri) {
        imageView.setVisibility(View.VISIBLE);
        ImageTransformation imageTransformation = new ImageTransformation(this);

        if (imageUri != null && !imageUri.toString().isEmpty()) {
            Picasso
                    .with(this)
                    .load(imageUri)
                    .transform(imageTransformation)
                    .into(imageView);

        }
    }

}
