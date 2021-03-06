package pl.codeinprogress.notes.injection;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.codeinprogress.notes.util.Analytics;
import pl.codeinprogress.notes.util.scheduler.AndroidSchedulerProvider;

@Module
public class ApplicationModule {

    private Context context;
    private Analytics analytics;
    private AndroidSchedulerProvider androidSchedulerProvider;

    public ApplicationModule(Context context) {
        this.context = context;
        this.androidSchedulerProvider = AndroidSchedulerProvider.getInstance();
        this.analytics = Analytics.getInstance(context);
    }

    @Provides @Singleton
    public Context providesContext() {
        return context;
    }

    @Provides @Singleton
    public SharedPreferences providesPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    @Provides @Singleton
    public AndroidSchedulerProvider providesScheduler() {
        return androidSchedulerProvider;
    }

    @Provides @Singleton
    public Analytics providesAnalytics() {
        return analytics;
    }

}
