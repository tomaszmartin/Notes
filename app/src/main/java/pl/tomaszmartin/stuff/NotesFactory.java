package pl.tomaszmartin.stuff;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

/**
 * Created by tomaszmartin on 24.03.15.
 */

public class NotesFactory {
    private ArrayList<Note> all;
    private ArrayList<Note> notes;
    private ArrayList<Note> articles;
    private ArrayList<Note> lists;
    private static NotesFactory notesFactory;
    private NotesDatabaseHelper notesDatabaseHelper;
    private Context context;
    private String tag = "__debug";

    private NotesFactory(Context context) {
        this.context = context;
        notesDatabaseHelper = NotesDatabaseHelper.get(context);
        all = notesDatabaseHelper.getData();
    }

    public int getNextId() {
        if (all.isEmpty()) {
            return 0;
        } else {
            ArrayList<Integer> ids = new ArrayList<>();
            for (Note note: all) {
                ids.add(note.getId());
                Log.d(tag, "current is: " + String.valueOf(note.getId()));
            }

            Collections.sort(ids);
            return ids.get(ids.size() - 1) + 1;
        }
    }

    public static NotesFactory get(Context context) {
        if (notesFactory == null) {
            notesFactory = new NotesFactory(context);
        }

        return notesFactory;
    }

    public NotesFactory get() {
        return notesFactory;
    }

    public ArrayList<Note> getAll() {
        return all;
    }

    public ArrayList<Note> getNotes() {
        notes = new ArrayList<>();
        for (Note item: all) {
            if (item.getType() == Note.NOTE) {
                Log.d("NotesFactory", "Item with title " + item.getTitle() + " is a note");
                notes.add(item);
            }
        }
        return notes;
    }

    public ArrayList<Note> getArticles() {
        articles = new ArrayList<>();
        for (Note item: all) {
            if (item.getType() == Note.ARTICLE) {
                Log.d("NotesFactory", "Item with title " + item.getTitle() + " is an article");
                articles.add(item);
            }
        }
        return articles;
    }

    public ArrayList<Note> getLists() {
        lists = new ArrayList<>();
        for (Note item: all) {
            if (item.getType() == Note.LIST) {
                Log.d("NotesFactory", "Item with title " + item.getTitle() + " is a list");
                lists.add(item);
            }
        }
        return lists;
    }

    public Note getNote(Integer id) {
        for (Note n: all) {
            if (n.getId().equals(id)) {
                return n;
            }
        }
        return null;
    }

    public void updateNote(Note note) {
        notesDatabaseHelper.updateData(note);
    }

    public void add(Note note) {
        notesDatabaseHelper.insertData(note);
    }

    public void registerContext(Context context) {
        this.context = context;
    }

    public void delete(Note note) {
        notesDatabaseHelper.deleteData(note);
    }

    public Note createNote(String name) {
        int id = getNextId();
        Note note = new Note(id, name, UUID.randomUUID().toString() + ".txt", Note.NOTE);
        add(note);

        return note;
    }

    public Note createList(String name) {
        int id = getNextId();
        Note note = new Note(id, name, UUID.randomUUID().toString() + ".txt", Note.LIST);
        add(note);

        return note;
    }

    public void createReadLater(String urlPath, NotesAdapter adapter) {
        AddReadLaterTask addReadLaterTask = new AddReadLaterTask(notesDatabaseHelper, this, urlPath, adapter);
        addReadLaterTask.execute();
    }

    public ArrayList<String> getCategories() {
        notes = getNotes();
        ArrayList<String> categories = new ArrayList<String>();
        categories.add(context.getString(R.string.all));
        categories.add(context.getString(R.string.personal));
        categories.add(context.getString(R.string.work));

        return categories;
    }

    public class AddReadLaterTask extends AsyncTask<String, String, Void> {

        private NotesFactory notesFactory;
        private String title;
        private String fileName;
        private String urlPath;
        private String img;
        private int id;
        private NotesAdapter adapter;
        private ProgressDialog progressDialog;

        public AddReadLaterTask(NotesDatabaseHelper notesDatabaseHelper, NotesFactory notesFactory, String urlPath, NotesAdapter adapter) {
            this.notesFactory = notesFactory;
            this.id = notesFactory.getNextId();
            this.urlPath = urlPath;
            this.fileName = UUID.randomUUID().toString() + ".txt";
            this.adapter = adapter;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(context);
            progressDialog.setMessage(context.getString(R.string.downloading_article));
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(String... params) {
            String token = "23a11ed2912cad58b2ea12d43114671acda24ddd";
            String result = "";
            try {
                String urlPrefix = String.format("http://readability.com/api/content/v1/parser?token=%s&url=", token);
                URL url = new URL(urlPrefix + urlPath);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream urlInputStream  = urlConnection.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(urlInputStream));
                StringBuilder builder = new StringBuilder();
                String line = "";
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                urlInputStream.close();
                result = builder.toString();
                Log.d("NotesFactory", result);
                JSONObject json = new JSONObject(result);
                String content = json.getString("content");
                title = json.getString("title");
                img = json.getString("lead_image_url");
                content = content.replaceAll("[<](/)?img[^>]*[>]", "");

                FileOutputStream fos = context.openFileOutput(fileName, Activity.MODE_PRIVATE);
                fos.write(content.getBytes());
                fos.close();

            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Note note = new Note(id, title, fileName, Note.ARTICLE);
            note.setImage(img);
            notesFactory.add(note);
            //adapter.add(note);
            //adapter.notifyDataSetChanged();
            //progressDialog.dismiss();
        }
    }

}
