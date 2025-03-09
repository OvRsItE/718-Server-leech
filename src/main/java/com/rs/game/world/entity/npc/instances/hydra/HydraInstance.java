package com.rs.game.world.entity.npc.instances.hydra;

import com.rs.game.map.RegionBuilder;
import com.rs.game.map.instance.RegionInstance;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.FadingScreen;

/**
 * @author ReverendDread
 * Created 2/18/2021 at 3:32 PM
 * @project 718---Server
 */
public class HydraInstance extends RegionInstance {

    public HydraInstance() {
        super(8, 168, 1280);
    }

    public static final void launch(final Player player) {
        final HydraInstance instance = new HydraInstance();
        instance.constructRegion();
        FadingScreen.fade(player, () -> {
            player.setNextPosition(instance.getLocation(1352, 10252, 0));
            player.getControlerManager().startControler("HydraController", instance);
        });
    }

}
