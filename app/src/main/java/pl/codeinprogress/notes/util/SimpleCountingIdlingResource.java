package pl.codeinprogress.notes.util;

import android.support.test.espresso.IdlingResource;

import java.util.concurrent.atomic.AtomicInteger;
import static com.google.common.base.Preconditions.checkNotNull;

public class SimpleCountingIdlingResource implements IdlingResource {

    private final String name;
    private volatile ResourceCallback resourceCallback;
    private final AtomicInteger counter = new AtomicInteger(0);

    public SimpleCountingIdlingResource(String name) {
        this.name = checkNotNull(name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isIdleNow() {
        return counter.get() == 0;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = callback;
    }

    public void increment() {
        counter.getAndIncrement();
    }

    public void decrement() {
        int counterValue = counter.getAndDecrement();
        if (counterValue == 0) {
            if (null != resourceCallback) {
                resourceCallback.onTransitionToIdle();
            }
        }
        if (counterValue < 0) {
            throw new IllegalArgumentException("Counter cannot be lower than 0!");
        }
    }

}

