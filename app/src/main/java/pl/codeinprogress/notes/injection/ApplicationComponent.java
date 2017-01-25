package pl.codeinprogress.notes.injection;

import javax.inject.Singleton;
import dagger.Component;
import pl.codeinprogress.notes.view.BaseActivity;
import pl.codeinprogress.notes.view.DetailsActivity;
import pl.codeinprogress.notes.view.NotesActivity;

@Singleton
@Component(modules = {ApplicationModule.class, NotesRepositoryModule.class})
public interface ApplicationComponent {

    void inject(BaseActivity target);

}
