package com.rs.game.world.entity.npc.instances.Godwars;

import com.rs.game.map.instance.RegionInstance;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.FadingScreen;

public class SaradominInstance extends RegionInstance {

    public SaradominInstance() {
        super(8, 364, 655);
    }

    public static final void launch(final Player player) {
        final SaradominInstance instance = new SaradominInstance();
        instance.constructRegion();
        FadingScreen.fade(player, () -> {
            player.setNextPosition(instance.getLocation(2923, 5257, 0));
            player.getControlerManager().startControler("SaradominController", instance);
        });
    }

}