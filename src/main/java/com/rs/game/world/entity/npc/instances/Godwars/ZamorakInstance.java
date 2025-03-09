package com.rs.game.world.entity.npc.instances.Godwars;

import com.rs.game.map.instance.RegionInstance;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.FadingScreen;

public class ZamorakInstance extends RegionInstance {

    public ZamorakInstance() {
        super(8, 364, 664);
    }

    public static final void launch(final Player player) {
        final ZamorakInstance instance = new ZamorakInstance();
        instance.constructRegion();
        FadingScreen.fade(player, () -> {
            player.setNextPosition(instance.getLocation(2925, 5333, 0));
            player.getControlerManager().startControler("ZamorakController", instance);
        });
    }

}