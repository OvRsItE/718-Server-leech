package com.rs.game.world.entity.npc.instances.Corp;

import com.rs.game.map.instance.RegionInstance;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.FadingScreen;

public class CorpInstance extends RegionInstance {

    public CorpInstance() {
        super(8, 369, 545);
    }

    public static final void launch(final Player player) {
        final CorpInstance instance = new CorpInstance();
        instance.constructRegion();
        FadingScreen.fade(player, () -> {
            player.setNextPosition(instance.getLocation(2970, 4384, 2));
            player.getControlerManager().startControler("CorpController", instance);
        });
    }

}