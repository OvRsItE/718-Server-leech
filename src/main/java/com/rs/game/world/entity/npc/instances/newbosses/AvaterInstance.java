package com.rs.game.world.entity.npc.instances.newbosses;

import com.rs.game.map.instance.RegionInstance;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.FadingScreen;

public class AvaterInstance extends RegionInstance {

    public AvaterInstance() {
        super(8, 259, 155);
    }

    public static final void launch(final Player player) {
        final AvaterInstance instance = new AvaterInstance();
        instance.constructRegion();
        FadingScreen.fade(player, () -> {
            player.setNextPosition(instance.getLocation(2089, 1244, 0));
            player.getControlerManager().startControler("AvatarController", instance);
        });
    }

}