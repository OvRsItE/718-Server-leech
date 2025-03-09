package com.rs.game.world.entity.npc.instances.EliteDungeon;

import com.rs.game.map.Position;
import com.rs.game.map.WorldObject;
import com.rs.game.world.entity.npc.NPC;
import com.sun.org.apache.xpath.internal.objects.XObject;

/**
 * @author ReverendDread
 * Created 2/18/2021 at 3:38 PM
 * @project 718---Server
 */
public class OlmClaws extends NPC {


    private static final long serialVersionUID = 4824558924770487553L;

    public OlmClaws(int id, Position tile) {
        super(id, tile, -1, 0, true, true);
        setForceMultiArea(true);
        setAtMultiArea(true);
        setCantWalk(true);
        setCantFollowUnderCombat(true);
        setCanRetaliate(false);
        faceNone();
        cancelFaceEntityNoCheck();

    }

    @Override
    public void processNPC() {
        faceNone();
        super.processNPC();
    }
}

