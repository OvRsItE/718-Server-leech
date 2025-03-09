package com.rs.cores.coroutines;

import com.rs.game.world.entity.Entity;
import kilim.Continuation;
import kilim.NotPausable;
import kilim.Pausable;
import kilim.Task;
import lombok.Setter;

import java.io.Serializable;
import java.util.function.Supplier;

public class CoroutineEvent {

    //Delay until the event is to be executed.
    private int delay;

    //The continuation coroutine.
    protected Continuation continuation;

    @Setter
    //The condition of which this event is canceled.
    private Supplier<Boolean> cancelCondition;

    public CoroutineEvent(EventConsumer consumer) {
        continuation = new Continuation() {

            @Override
            public void execute() throws Pausable {
                consumer.accept(CoroutineEvent.this);
            }

        };
    }

    public final boolean tick() {
        if (delay > 0) {
            if (--delay > 0)
                return true;
            if (cancelCondition != null && cancelCondition.get()) {
                return false; //removes the event
            }
        }
        try {
            return !continuation.run();
        } catch (NotPausable e) {
            e.printStackTrace();
            return false;
        }
    }

    public final void delay(int delay) throws Pausable {
        this.delay = delay;
        Task.yield();
    }

    public final void waitForMovement(Entity entity) throws Pausable {
        while (entity.hasWalkSteps())
            delay(1);
    }

    @FunctionalInterface
    public interface EventConsumer {

        void accept(CoroutineEvent event) throws Pausable;

    }

}