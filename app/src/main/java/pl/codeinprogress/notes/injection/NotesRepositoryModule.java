package pl.codeinprogress.notes.injection;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import pl.codeinprogress.notes.model.NotesDataSource;
import pl.codeinprogress.notes.model.NotesRepository;
import pl.codeinprogress.notes.model.local.LocalNotesDataSource;
import pl.codeinprogress.notes.util.SchedulerProvider;

@Module
public class NotesRepositoryModule {

    private Context context;
    private NotesRepository repository;

    public NotesRepositoryModule(Context context) {
        this.context = context;
        SchedulerProvider schedulerProvider = SchedulerProvider.getInstance();
        NotesDataSource dataSource = LocalNotesDataSource.getInstance(context, schedulerProvider);
        this.repository = NotesRepository.getInstance(dataSource);
    }

    @Provides
    public NotesRepository providesNotesRepository() {
        return repository;
    }

}
