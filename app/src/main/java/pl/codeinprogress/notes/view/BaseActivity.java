package pl.codeinprogress.notes.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;

import com.google.firebase.analytics.FirebaseAnalytics;

import javax.inject.Inject;

import io.realm.Realm;
import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.injection.ApplicationComponent;
import pl.codeinprogress.notes.injection.ApplicationModule;
import pl.codeinprogress.notes.injection.DaggerApplicationComponent;
import pl.codeinprogress.notes.injection.NotesRepositoryModule;
import pl.codeinprogress.notes.model.NotesRepository;
import pl.codeinprogress.notes.util.Analytics;
import pl.codeinprogress.notes.util.SchedulerProvider;

public class BaseActivity extends AppCompatActivity {

    @Inject
    NotesRepository injectedRepository;
    @Inject
    SchedulerProvider scheduler;
    @Inject
    Analytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ApplicationComponent component = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .notesRepositoryModule(new NotesRepositoryModule(this))
                .build();
        component.inject(this);
        
        analytics.sendScreen(getTag());
    }

    public String getTag() {
        return this.getClass().getSimpleName();
    }

    public void log(String message) {
        Log.d(getTag(), message);
    }

    public void switchNightMode() {
        boolean isNight = getResources().getBoolean(R.bool.isNight);
        if (isNight) {
            setDay();
        } else {
            setNight();
        }
    }

    public void sendEvent(String category, String action, String label, long value) {
        analytics.sendEvent(category, action, label, value);
    }

    public NotesRepository getRepository() {
        return injectedRepository;
    }

    public SchedulerProvider getScheduler() {
        return scheduler;
    }



    private void setDay() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        recreate();
    }

    private void setNight() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        recreate();
    }

}
