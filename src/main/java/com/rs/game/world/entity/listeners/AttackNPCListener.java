package com.rs.game.world.entity.listeners;

import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;

/**
 * @author ReverendDread
 * Created 3/2/2021 at 9:41 AM
 * @project 718---Server
 */
public interface AttackNPCListener {

    boolean allow(Player player, NPC npc);

}
