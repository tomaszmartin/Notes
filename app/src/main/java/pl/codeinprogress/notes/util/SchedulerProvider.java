package pl.codeinprogress.notes.util;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class SchedulerProvider {

    @Nullable
    private static SchedulerProvider instance;

    private SchedulerProvider() {
    }

    public static SchedulerProvider getInstance() {
        if (instance == null) {
            instance = new SchedulerProvider();
        }

        return instance;
    }

    @NonNull
    public Scheduler computation() {
        return Schedulers.computation();
    }

    @NonNull
    public Scheduler io() {
        return Schedulers.io();
    }

    @NonNull
    public Scheduler ui() {
        return AndroidSchedulers.mainThread();
    }

}
