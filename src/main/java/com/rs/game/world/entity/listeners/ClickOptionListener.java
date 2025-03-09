package com.rs.game.world.entity.listeners;

import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;

/**
 * @author ReverendDread
 * Created 3/12/2021 at 6:59 AM
 * @project 718---Server
 */
@FunctionalInterface
public interface ClickOptionListener {

    /**
     *
     * @param player
     *          the player doing the action.
     * @param npc
     *          the npc.
     * @return
     *          true if the npc faces the player, false otherwise.
     */
    boolean handle(Player player, NPC npc);

}
