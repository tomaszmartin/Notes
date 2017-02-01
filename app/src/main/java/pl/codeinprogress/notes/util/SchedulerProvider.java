package pl.codeinprogress.notes.util;

import rx.Scheduler;

public interface SchedulerProvider {

    public Scheduler ui();
    public Scheduler io();
    public Scheduler computation();

}
