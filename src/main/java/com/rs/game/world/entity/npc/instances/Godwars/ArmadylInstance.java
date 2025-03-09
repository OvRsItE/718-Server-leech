package com.rs.game.world.entity.npc.instances.Godwars;

import com.rs.game.map.instance.RegionInstance;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.FadingScreen;

public class ArmadylInstance extends RegionInstance {

    public ArmadylInstance() {
        super(8, 352, 661);
    }

    public static final void launch(final Player player) {
        final ArmadylInstance instance = new ArmadylInstance();
        instance.constructRegion();
        FadingScreen.fade(player, () -> {
            player.setNextPosition(instance.getLocation(2835, 5294, 0));
            player.getControlerManager().startControler("ArmadylController", instance);
        });
    }

}