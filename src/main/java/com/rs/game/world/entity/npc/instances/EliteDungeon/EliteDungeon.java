package com.rs.game.world.entity.npc.instances.EliteDungeon;

import com.rs.cores.CoresManager;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.MapBuilder;
import com.rs.game.map.RegionBuilder;
import com.rs.game.map.instance.RegionInstance;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.player.ControlerManager;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.FadingScreen;
import com.rs.utility.Logger;

import java.util.List;

/**
 * @author ReverendDread
 * Created 2/18/2021 at 3:32 PM
 * @project 718---Server
 */
public class EliteDungeon {

    public static void launch(final Player player, final Player target) {
        EliteDungeonOne dungeon = target == null ? new EliteDungeonOne(player) : target.getEliteDungeonOne();
        if (dungeon.isStarted()) {
            player.sm("You can't join a dungeon that is already started.");
            return;
        }
        FadingScreen.fade(player, () -> {
            dungeon.enter(player);
        });
    }

}
