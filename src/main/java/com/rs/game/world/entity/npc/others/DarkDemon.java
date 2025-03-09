package com.rs.game.world.entity.npc.others;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;

/**
 * @author ReverendDread
 * Created 3/22/2021 at 1:45 AM
 * @project 718---Server
 */
public class DarkDemon extends NPC {

    public DarkDemon(int id, Position tile, int mapAreaNameHash, int faceDirection, boolean canBeAttackFromOutOfArea, boolean spawned) {
        super(id, tile, mapAreaNameHash, faceDirection, canBeAttackFromOutOfArea, spawned);
    }

    @Override
    public double getMagePrayerMultiplier() {
        return 0.25;
    }

}
