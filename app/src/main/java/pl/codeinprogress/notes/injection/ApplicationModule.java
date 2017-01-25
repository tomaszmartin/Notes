package pl.codeinprogress.notes.injection;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import pl.codeinprogress.notes.util.SchedulerProvider;

@Module
public class ApplicationModule {

    private Context context;
    private SchedulerProvider schedulerProvider;

    public ApplicationModule(Context context) {
        this.context = context;
        this.schedulerProvider = SchedulerProvider.getInstance();
    }

    @Provides @Singleton
    public Context providesContext() {
        return context;
    }

    @Provides @Singleton
    public SchedulerProvider providesScheduler() {
        return schedulerProvider;
    }

}
