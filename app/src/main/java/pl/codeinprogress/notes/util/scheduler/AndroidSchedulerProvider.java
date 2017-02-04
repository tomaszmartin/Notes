package pl.codeinprogress.notes.util.scheduler;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class AndroidSchedulerProvider implements SchedulerProvider {

    @Nullable
    private static AndroidSchedulerProvider instance;

    private AndroidSchedulerProvider() {
    }

    public static AndroidSchedulerProvider getInstance() {
        if (instance == null) {
            instance = new AndroidSchedulerProvider();
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
