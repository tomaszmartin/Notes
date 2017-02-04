package pl.codeinprogress.notes.injection;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import pl.codeinprogress.notes.model.NotesDataSource;
import pl.codeinprogress.notes.model.NotesRepository;
import pl.codeinprogress.notes.model.local.LocalNotesDataSource;
import pl.codeinprogress.notes.util.scheduler.AndroidSchedulerProvider;

@Module
public class NotesRepositoryModule {

    private Context context;
    private NotesRepository repository;

    public NotesRepositoryModule(Context context) {
        this.context = context;
        AndroidSchedulerProvider androidSchedulerProvider = AndroidSchedulerProvider.getInstance();
        NotesDataSource dataSource = LocalNotesDataSource.getInstance(context, androidSchedulerProvider);
        this.repository = NotesRepository.getInstance(dataSource);
    }

    @Provides
    public NotesRepository providesNotesRepository() {
        return repository;
    }

}
