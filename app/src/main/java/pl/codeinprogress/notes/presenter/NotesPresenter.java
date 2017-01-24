package pl.codeinprogress.notes.presenter;

import android.support.annotation.NonNull;
import android.util.Log;

import java.util.List;
import java.util.UUID;

import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.model.NotesRepository;
import pl.codeinprogress.notes.util.EspressoIdlingResource;
import pl.codeinprogress.notes.util.SchedulerProvider;
import pl.codeinprogress.notes.view.views.NotesView;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
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

    public NotesPresenter(@NonNull NotesView view, @NonNull NotesRepository repository, @NonNull SchedulerProvider provider) {
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
                .subscribeOn(schedulerProvider.computation())
                .observeOn(schedulerProvider.ui())
                .doOnTerminate(() -> {
                    if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                        EspressoIdlingResource.decrement();
                    }
                })
                .subscribe(notes -> {view.showNotes(notes);});

        subscriptions.add(subscription);
    }

    public void deleteNote(String noteId) {
        repository.deleteNote(noteId);
    }

    public void addNote() {
        Note note = new Note();
        String id = UUID.randomUUID().toString();
        note.setId(id);
        repository.saveNote(note);
        view.openEditView(id);
    }

}
