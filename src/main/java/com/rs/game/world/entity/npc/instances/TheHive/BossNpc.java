package com.rs.game.world.entity.npc.instances.TheHive;

import com.rs.game.map.Bounds;
import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;

/**
 * @author ReverendDread
 * Created 2/18/2021 at 3:38 PM
 * @project 718---Server
 */
public class BossNpc extends NPC {




    private static final long serialVersionUID = 4824558924770487553L;


    public BossNpc(int id, Position tile) {
        super(id, tile, -1, 0, true, true);
        setForceMultiArea(true);
        setAtMultiArea(true);
        setCantWalk(true);
        setCapDamage(700);
        setCantFollowUnderCombat(true);

    }

}

