package pl.codeinprogress.notes.presenter;

import android.support.annotation.NonNull;

import pl.codeinprogress.notes.model.NotesRepository;
import pl.codeinprogress.notes.util.SchedulerProvider;
import pl.codeinprogress.notes.view.views.NotesView;
import rx.subscriptions.CompositeSubscription;

import static com.google.common.base.Preconditions.checkNotNull;

public class NotesPresenter {

    @NonNull
    private NotesRepository repository;
    @NonNull
    private NotesView view;
    @NonNull
    private SchedulerProvider schedulerProvider;
    @NonNull
    private CompositeSubscription subscriptions;

    public NotesPresenter(@NonNull NotesRepository repository, @NonNull NotesView view, @NonNull SchedulerProvider provider) {
        this.schedulerProvider = checkNotNull(provider);
        this.repository = checkNotNull(repository);
        this.view = checkNotNull(view);
        this.subscriptions = new CompositeSubscription();
    }

    

}
