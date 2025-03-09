package com.rs.game.world.entity.npc.instances.newbosses;

import com.rs.game.map.instance.RegionInstance;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.FadingScreen;

public class SunfreetInstance extends RegionInstance {

    public SunfreetInstance() {
        super(8, 251, 147);
    }

    public static final void launch(final Player player) {
        final SunfreetInstance instance = new SunfreetInstance();
        instance.constructRegion();
        FadingScreen.fade(player, () -> {
            player.setNextPosition(instance.getLocation(2019, 1180, 0));
            player.getControlerManager().startControler("SunfreetController", instance);
        });
    }

}