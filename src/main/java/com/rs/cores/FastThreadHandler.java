package com.rs.cores;

/**
 * @author David O'Neill
 */
public final class FastThreadHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
      //  Logger.threadFatal("(" + thread.getName() + ", fast pool) - Printing trace");
        throwable.printStackTrace();
    }
}
