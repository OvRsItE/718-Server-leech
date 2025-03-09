package com.rs.game.world.entity.npc.instances.EliteDungeon;

import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;

/**
 * @author ReverendDread
 * Created 2/18/2021 at 3:38 PM
 * @project 718---Server
 */
public class Olm extends NPC {


    private static final long serialVersionUID = 4824558924770487553L;

    public Olm(int id, Position tile) {
        super(id, tile, -1, 0, true, true);
        setForceMultiArea(true);
        setAtMultiArea(true);
        setCantWalk(true);
        setCantFollowUnderCombat(true);


    }

    @Override
    public void processNPC() {
        super.processNPC();
    }
}

