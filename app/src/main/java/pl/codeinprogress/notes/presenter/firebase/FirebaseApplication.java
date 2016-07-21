package pl.codeinprogress.notes.presenter.firebase;

import android.app.Application;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.storage.FirebaseStorage;
import pl.codeinprogress.notes.R;

/**
 * Created by tomaszmartin on 05.07.2016.
 */
public class FirebaseApplication extends Application {

    private FirebaseAnalytics analytics;
    private FirebaseAuth auth;
    private FirebaseDatabase database;
    private FirebaseRemoteConfig configuration;
    private FirebaseStorage storage;
    private Auth authHandler;

    @Override
    public void onCreate() {
        super.onCreate();
        analytics = FirebaseAnalytics.getInstance(this);
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        database.setPersistenceEnabled(true);
        storage = FirebaseStorage.getInstance();
        configuration = FirebaseRemoteConfig.getInstance();
        configuration.setDefaults(R.xml.firebase);
        authHandler = Auth.getInstance(this);
    }

    public FirebaseAnalytics getAnalytics() {
        return analytics;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }

    public FirebaseDatabase getDatabase() {
        return database;
    }

    public FirebaseRemoteConfig getConfiguration() {
        return configuration;
    }

    public FirebaseStorage getStorage() {
        return storage;
    }

    public Auth getAuthHandler() {
        return authHandler;
    }
}
