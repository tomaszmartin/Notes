package pl.codeinprogress.notes.injection;

import javax.inject.Singleton;
import dagger.Component;
import pl.codeinprogress.notes.view.NotesActivity;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    void inject(NotesActivity target);

}
