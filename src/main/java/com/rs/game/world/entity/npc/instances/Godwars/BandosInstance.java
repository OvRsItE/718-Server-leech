package com.rs.game.world.entity.npc.instances.Godwars;

import com.rs.game.map.instance.RegionInstance;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.FadingScreen;

public class BandosInstance extends RegionInstance {

    public BandosInstance() {
        super(8, 354, 667);
    }

    public static final void launch(final Player player) {
        final BandosInstance instance = new BandosInstance();
        instance.constructRegion();
        FadingScreen.fade(player, () -> {
            player.setNextPosition(instance.getLocation(2861, 5357, 0));
            player.getControlerManager().startControler("BandosController", instance);
        });
    }

}