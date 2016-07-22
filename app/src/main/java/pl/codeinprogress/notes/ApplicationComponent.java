package pl.codeinprogress.notes;

import javax.inject.Singleton;
import dagger.Component;
import pl.codeinprogress.notes.view.MainActivity;

@Singleton
@Component(modules = {ApplicationModule.class})
public interface ApplicationComponent {

    void inject(MainActivity target);

}
