package pl.codeinprogress.notes.presenter;

import android.support.annotation.NonNull;

import java.util.UUID;

import pl.codeinprogress.notes.R;
import pl.codeinprogress.notes.model.Note;
import pl.codeinprogress.notes.model.NotesRepository;
import pl.codeinprogress.notes.util.espresso.EspressoIdlingResource;
import pl.codeinprogress.notes.util.scheduler.SchedulerProvider;
import pl.codeinprogress.notes.view.views.NotesView;

import static com.google.common.base.Preconditions.checkNotNull;

public class NotesPresenter {

    @NonNull
    private NotesRepository repository;
    @NonNull
    private NotesView view;
    @NonNull
    private SchedulerProvider androidSchedulerProvider;

    public NotesPresenter(@NonNull NotesView view, @NonNull NotesRepository repository, @NonNull SchedulerProvider provider) {
        this.androidSchedulerProvider = checkNotNull(provider);
        this.repository = checkNotNull(repository);
        this.view = checkNotNull(view);
    }

    public void loadNotes() {
        EspressoIdlingResource.increment();
        repository.getNotes()
                .subscribeOn(androidSchedulerProvider.computation())
                .observeOn(androidSchedulerProvider.ui())
                .doOnTerminate(() -> {
                    if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                        EspressoIdlingResource.decrement();
                    }
                })
                .doOnError(error -> view.showErrorMessage(R.string.error_loading))
                .subscribe(notes -> view.showNotes(notes));
    }

    public void deleteNote(@NonNull String noteId) {
        repository.deleteNote(noteId);
    }

    public void queryNotes(@NonNull String query) {
        EspressoIdlingResource.increment();
        repository.queryNotes(query)
                .subscribeOn(androidSchedulerProvider.computation())
                .observeOn(androidSchedulerProvider.ui())
                .doOnTerminate(() -> {
                    if (!EspressoIdlingResource.getIdlingResource().isIdleNow()) {
                        EspressoIdlingResource.decrement();
                    }
                })
                .doOnError(error -> view.showErrorMessage(R.string.error_searching))
                .subscribe(notes -> view.showNotes(notes));
    }

    public void addNote() {
        Note note = new Note();
        String id = UUID.randomUUID().toString();
        note.setId(id);
        repository.addNote(id);
        view.openEditView(id);
    }

}
