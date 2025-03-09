package com.rs.cores.coroutines;

import com.google.common.collect.Queues;
import com.rs.cores.WorldThread;

import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author ReverendDread
 * Created 2/18/2021 at 1:12 PM
 * @project 718---Server
 */
public class CoroutineWorker {

    private static final ConcurrentLinkedQueue<CoroutineEvent> EVENTS = Queues.newConcurrentLinkedQueue();

    public static void tick() {
        EVENTS.removeIf(event -> !event.tick());
    }

    public static CoroutineEvent createEvent(CoroutineEvent.EventConsumer consumer) {
        return new CoroutineEvent(consumer);
    }

    public static CoroutineEvent startEvent(CoroutineEvent.EventConsumer consumer) {
        CoroutineEvent event = new CoroutineEvent(consumer);
        if (!WorldThread.isPast(WorldThread.Stage.LOGIC)) {
            if (!event.tick())
                return event;
        }
        EVENTS.add(event);
        return event;
    }

}
