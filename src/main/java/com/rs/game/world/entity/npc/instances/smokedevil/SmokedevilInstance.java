package com.rs.game.world.entity.npc.instances.smokedevil;

import com.rs.game.map.instance.RegionInstance;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.FadingScreen;

public class SmokedevilInstance extends RegionInstance {

    public SmokedevilInstance() {
        super(8, 357, 315);
    }

    public static final void launch(final Player player) {
        final SmokedevilInstance instance = new SmokedevilInstance();
        instance.constructRegion();
        FadingScreen.fade(player, () -> {
            player.setNextPosition(instance.getLocation(2888, 2540, 0));
            player.getControlerManager().startControler("SmokedevilController", instance);
        });
    }

}