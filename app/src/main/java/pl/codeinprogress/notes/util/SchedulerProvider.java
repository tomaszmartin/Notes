package pl.codeinprogress.notes.util;

import android.support.annotation.NonNull;

import rx.Scheduler;

public interface SchedulerProvider {

    @NonNull
    Scheduler computation();

    @NonNull
    Scheduler io();

    @NonNull
    Scheduler ui();

}
