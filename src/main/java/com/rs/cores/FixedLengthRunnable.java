package com.rs.cores;

import java.util.concurrent.Future;

/**
 * A {@link Runnable} subtype intended to run for a fixed iteration period
 * in the context of a {@link java.util.concurrent.ScheduledExecutorService}.
 * @author David O'Neill
 */
public abstract class FixedLengthRunnable implements Runnable {

    private Future<?> future;

    /**
     * Iteratively runs {@code repeat()} until
     * it returns {@code false}, at which point
     * this runnable is cancelled from the executor.
     */
    @Override
    public void run() {
        if(!repeat()) cancel();
    }

    /**
     * Defines the intended logic to be repeated, and
     * should return {@code false} when the runnable
     * should be cancelled and dequeued.
     * @return whether or not to run another iteration
     */
    public abstract boolean repeat();

    /**
     * Assign this runnables {@link Future} reference.
     * @param future - the future associated with this runnable
     */
    void assignFuture(Future<?> future) {
        this.future = future;
    }


    private void cancel() {
        future.cancel(false);
        CoresManager.purgeSlowExecutor();
    }

    
    public void stopNow(boolean interrupt) {
        future.cancel(interrupt);
        CoresManager.purgeSlowExecutor();
    }
}
