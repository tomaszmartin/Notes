package pl.codeinprogress.notes.presenter;

import android.support.annotation.NonNull;

import pl.codeinprogress.notes.model.NotesRepository;
import pl.codeinprogress.notes.util.EspressoIdlingResource;
import pl.codeinprogress.notes.util.SchedulerProvider;
import pl.codeinprogress.notes.view.views.NotesView;
import rx.Observable;
import rx.Subscription;
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

    public void loadNotes() {
        EspressoIdlingResource.increment();
        subscriptions.clear();
        Subscription subscription = repository
                .getNotes()
                .flatMap(notes -> Observable.from(notes))
                .toList()
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .doOnTerminate(() -> {
                    if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                        EspressoIdlingResource.decrement();
                    }
                })
                .subscribe(notes -> view.showNotes(notes), throwable -> view.showLoadingError(), () -> view.hideLoadingIndicator());

        subscriptions.add(subscription);
    }

}
