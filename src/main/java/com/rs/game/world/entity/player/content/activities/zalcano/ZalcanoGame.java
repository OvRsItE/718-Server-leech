package com.rs.game.world.entity.player.content.activities.zalcano;

import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author ReverendDread
 * Created 3/12/2021 at 6:41 AM
 * @project 718---Server
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ZalcanoGame {

    private static final int ATTACKABLE = 9049;
    private static final int MINEABLE = 9050;

    public static boolean handleZalcanoClick(Player player, NPC zalcano) {
        if (zalcano != null && !zalcano.isDead() && !zalcano.isFinished()) {
            if (zalcano.getId() == NPC.asOSRS(ATTACKABLE)) {
                //TODO start event for throwing stones
            } else if (zalcano.getId() == NPC.asOSRS(MINEABLE)) {

            }
        }
        return false;
    }

}
