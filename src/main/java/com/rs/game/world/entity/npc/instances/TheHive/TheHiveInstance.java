package com.rs.game.world.entity.npc.instances.TheHive;

import com.rs.game.world.entity.npc.instances.EliteDungeon.EliteDungeonOne;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.FadingScreen;

/**
 * @author ReverendDread
 * Created 2/18/2021 at 3:32 PM
 * @project 718---Server
 */
public class TheHiveInstance {

    public static void launch(final Player player, final Player target) {
        TheHive dungeon = target == null ? new TheHive(player) : target.getThehive();
        if (dungeon.isStarted()) {
            player.sm("You can't join a dungeon that is already started.");
            return;
        }
        FadingScreen.fade(player, () -> {
            dungeon.enter(player);
        });
    }

}
