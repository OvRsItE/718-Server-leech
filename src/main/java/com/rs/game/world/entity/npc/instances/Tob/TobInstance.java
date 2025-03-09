package com.rs.game.world.entity.npc.instances.Tob;

import com.rs.game.map.instance.RegionInstance;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.FadingScreen;

public class TobInstance extends RegionInstance {

    public TobInstance() {
        super(32, 392, 528);
    }

    public static final void launch(final Player player) {
        final TobInstance instance = new TobInstance();
        instance.constructRegion();
        FadingScreen.fade(player, () -> {
            player.setNextPosition(instance.getLocation(3161, 4459, 0));
        });
    }

}