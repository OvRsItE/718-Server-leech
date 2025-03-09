package com.rs.utility;

import com.rs.cores.WorldThread;

public class TickDelay {

    private long end;

    public void reset() {
        end = 0;
    }

    public void setEnd(long newEnd) {
        end = newEnd;
    }

    public void delay(int ticks) {
        end = WorldThread.WORLD_CYCLE + ticks;
    }

    public boolean allowed() {
        return WorldThread.WORLD_CYCLE >= end;
    }

    public boolean allowed(int extra) {
        return WorldThread.WORLD_CYCLE >= (end + extra);
    }

    public int remaining() {
        return (int) (end - WorldThread.WORLD_CYCLE);
    }

    public int remainingToMins() {
        return remaining() / (1000 * 60 / 600);
    }

    public boolean finished() {
        return WorldThread.WORLD_CYCLE > end;
    }

}
