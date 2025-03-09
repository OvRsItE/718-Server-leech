package com.rs.game.world.entity.npc.combat.impl.hydra;

import com.rs.game.map.Bounds;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.updating.impl.Graphics;

public enum FireArea {
    WEST(-8, 0, -1, 4,
            new Position[] {
                    new Position(-1, 4, 0),
                    new Position(-1, 3, 0),
                    new Position(-1, 2, 0),
                    new Position(-1, 1, 0)
            }, new int[]{-1, 0}),
    NORTH_WEST(-8, 5, -1, 14,
            new Position[] {
                    new Position(-1, 4, 0),
                    new Position(-1, 5, 0),
                    new Position(-1, 6, 0),
                    new Position(0, 6, 0),
                    new Position(1, 6, 0),
            }, new int[]{-1, 1}),
    NORTH(0, 5, 5, 14,
            new Position[] {
                    new Position(1, 6, 0),
                    new Position(2, 6, 0),
                    new Position(3, 6, 0),
                    new Position(4, 6, 0),
            }, new int[]{0, 1}),
    NORTH_EAST(6, 5, 13, 13,
            new Position[] {
                    new Position(6, 4, 0),
                    new Position(6, 5, 0),
                    new Position(6, 6, 0),
                    new Position(5, 6, 0),
                    new Position(4, 6, 0),

            }, new int[]{1, 1}),
    EAST(5, 0, 13, 5,
            new Position[] {
                    new Position(6, 1, 0),
                    new Position(6, 2, 0),
                    new Position(6, 3, 0),
                    new Position(6, 4, 0),
            }, new int[]{1, 0}),
    SOUTH_EAST(5, -8, 13, -1,
            new Position[] {
                    new Position(6, 1, 0),
                    new Position(6, 0, 0),
                    new Position(6, -1, 0),
                    new Position(5, -1, 0),
                    new Position(4, -1, 0),
            }, new int[]{1, -1}),
    SOUTH(0, -9, 5, 0,
            new Position[] {
                    new Position(1, -1, 0),
                    new Position(2, -1, 0),
                    new Position(3, -1, 0),
                    new Position(4, -1, 0),
            }, new int[]{0, -1}),
    SOUTH_WEST(-8, -8, 0, -1,
            new Position[] {
                    new Position(-1, 1, 0),
                    new Position(-1, 0, 0),
                    new Position(-1, -1, 0),
                    new Position(0, -1, 0),
                    new Position(1, -1, 0),
            }, new int[]{-1, -1});

    int swX, swY, neX, neY;

    FireArea(int swX, int swY, int neX, int neY, Position[] waveStart, int[] waveStep) {
        this.swX = swX;
        this.swY = swY;
        this.neX = neX;
        this.neY = neY;
        this.waveStart = waveStart;
        this.waveStep = waveStep;
    }

    Position[] waveStart;
    int[] waveStep;

    public FireArea[] getAdjacents() {
        int previous = ordinal() - 1;
        if (previous < 0)
            previous = values().length + previous;
        int next = ordinal() + 1;
        return new FireArea[]{values()[previous % values().length], values()[next % values().length]};
    }

    public void coverInFire(NPC hydra) {
        Position[] wave = new Position[waveStart.length];
        for (int i = 0; i < wave.length; i++)
            wave[i] = hydra.getSpawnPosition().relative(waveStart[i].getX(), waveStart[i].getY());
        for (int i = 0; i < 8; i++) {
            for (Position pos : wave) {
                World.sendGraphics(hydra, Graphics.createOSRS(1668, 50 + i * 5), pos);
                pos.translate(waveStep[0], waveStep[1], 0);
            }
        }
    }

    public boolean isInArea(Entity target, NPC hydra) {
        Bounds bounds = getBounds(hydra);
        if (target.getPosition().inBounds(bounds))
            return true;
        return false;
    }

    public Bounds getBounds(NPC hydra) {
        return new Bounds(hydra.getSpawnPosition().relative(swX, swY), hydra.getSpawnPosition().relative(neX, neY), hydra.getSpawnPosition().getZ());
    }

    public boolean isInFireArea(Entity target, NPC hydra) { // there's probably a math based way to do this, but right now i can't
        Position[] wave = new Position[waveStart.length];
        for (int i = 0; i < wave.length; i++)
            wave[i] = hydra.getSpawnPosition().relative(waveStart[i].getX(), waveStart[i].getY());
        for (int i = 0; i < 8; i++) {
            for (Position pos : wave) {
                if (target.getPosition().equals(pos))
                    return true;
                pos.translate(waveStep[0], waveStep[1], 0);
            }
        }
        return false;
    }

    public static FireArea getTargetArea(Entity target, NPC hydra) {
        for (FireArea area : values()) {
            if (area.isInArea(target, hydra))
                return area;
        }
        return null;
    }

}
