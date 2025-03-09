package com.rs.game.world.entity.npc.instances.hydra;

import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.updating.impl.Animation;

/**
 * @author ReverendDread
 * Created 2/18/2021 at 3:38 PM
 * @project 718---Server
 */
public class AlchemicalHydraNPC extends NPC {

    public AlchemicalHydraNPC(int id, Position tile) {
        super(id, tile, -1, 0, true, false);
    }

    @Override
    public void sendDeath(Entity source) {
        addEvent(e -> {
            animate(Animation.createOSRS(8257));
            e.delay(3);
            transformIntoNPC(NPC.asOSRS(8622));
            animate(Animation.createOSRS(8258));
            super.sendDeath(source);
        });
    }
}
