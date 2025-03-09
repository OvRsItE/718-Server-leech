package com.rs.game.world.entity.npc.instances.newbosses;

import com.rs.game.map.instance.RegionInstance;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.FadingScreen;

public class AcidicInstance extends RegionInstance {

    public AcidicInstance() {
        super(8, 251, 155);
    }

    public static final void launch(final Player player) {
        final AcidicInstance instance = new AcidicInstance();
        instance.constructRegion();
        FadingScreen.fade(player, () -> {
            player.setNextPosition(instance.getLocation(2018, 1245, 0));
            player.getControlerManager().startControler("AcidicController", instance);
        });
    }

}