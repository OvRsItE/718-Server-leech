package com.rs.game.world.entity.npc.others;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;

/**
 * @author ReverendDread
 * Created 3/27/2021 at 11:04 PM
 * @project 718---Server
 */
public class AmethystDragonMinion extends NPC {

    public AmethystDragonMinion(int id, Position tile, int mapAreaNameHash, int faceDirection, boolean canBeAttackFromOutOfArea, boolean spawned) {
        super(id, tile, mapAreaNameHash, faceDirection, canBeAttackFromOutOfArea, spawned);
        setForceMultiArea(true);
        setForceAgressive(true);
    }

}
