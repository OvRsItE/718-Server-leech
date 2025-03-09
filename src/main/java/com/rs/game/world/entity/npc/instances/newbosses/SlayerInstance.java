package com.rs.game.world.entity.npc.instances.newbosses;

import com.rs.game.map.instance.RegionInstance;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.FadingScreen;

public class SlayerInstance extends RegionInstance {

    public SlayerInstance() {
        super(8, 251, 147);
    }

    public static final void launch(final Player player) {
        final SlayerInstance instance = new SlayerInstance();
        instance.constructRegion();
        FadingScreen.fade(player, () -> {
            player.setNextPosition(instance.getLocation(2019, 1180, 0));
            player.getControlerManager().startControler("SlayerController", instance);
        });
    }

}