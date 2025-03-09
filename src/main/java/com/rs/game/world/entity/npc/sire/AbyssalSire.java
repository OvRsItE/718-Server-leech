package com.rs.game.world.entity.npc.sire;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;

/**
 * @author ReverendDread
 * Created 3/2/2021 at 11:13 PM
 * @project 718---Server
 */
public class AbyssalSire extends NPC {

    public AbyssalSire(int id, Position tile, int mapAreaNameHash, int faceDirection, boolean canBeAttackFromOutOfArea, boolean spawned) {
        super(id, tile, mapAreaNameHash, faceDirection, canBeAttackFromOutOfArea, spawned);
    }

}
