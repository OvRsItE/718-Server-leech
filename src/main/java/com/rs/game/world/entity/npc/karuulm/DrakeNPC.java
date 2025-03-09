package com.rs.game.world.entity.npc.karuulm;

import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.updating.impl.Animation;

/**
 * @author ReverendDread
 * Created 3/13/2021 at 4:46 AM
 * @project 718---Server
 */
public class DrakeNPC extends NPC {

    public DrakeNPC(int id, Position tile, int mapAreaNameHash, int faceDirection, boolean canBeAttackFromOutOfArea, boolean spawned) {
        super(id, tile, mapAreaNameHash, faceDirection, canBeAttackFromOutOfArea, spawned);
    }

    @Override
    public void sendDeath(Entity source) {
        addEvent(event -> {
            animate(Animation.createOSRS(8277));
            event.delay(1);
            transformIntoNPC(NPC.asOSRS(8613));
            animate(Animation.createOSRS(8278));
            super.sendDeath(source);
        });
    }
}
