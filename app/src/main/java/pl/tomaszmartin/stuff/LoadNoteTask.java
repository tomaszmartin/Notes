package pl.tomaszmartin.stuff;

import android.content.Context;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;

/**
 * Created by tomaszmartin on 21.06.2015.
 */
public class LoadNoteTask extends AsyncTask<String, Void, String> {

    private Context context;
    private TextView view;

    public LoadNoteTask(Context context, TextView view) {
        this.context = context;
        this.view = view;
    }

    @Override
    protected String doInBackground(String... strings) {
        String fileName = strings[0];
        String content = "";

        try {
            FileInputStream fis = context.openFileInput(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(new DataInputStream(fis)));
            String line;
            while ((line = reader.readLine()) != null) {
                content = content + line + "\n";
            }

            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return content;
    }

    @Override
    protected void onPostExecute(String contents) {
        view.setText(contents);
        view.setVisibility(View.VISIBLE);
    }

}
