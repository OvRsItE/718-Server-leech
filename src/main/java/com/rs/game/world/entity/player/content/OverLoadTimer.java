package com.rs.game.world.entity.player.content;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.world.entity.player.Player;

public class OverLoadTimer {
    
    public static void startTime(final Player p) {
        WorldTasksManager.schedule(new WorldTask() {
            @Override
            public void run() {
            	if (p.overloadMin >= 0 && p.overloadCount > 0) {
                p.overloadCount--;
                p.getPackets().sendIComponentText(3000, 5, ""+OverLoadTimer.getTime(p)+"");
            	} else {
				p.getPackets().sendHideIComponent(3000, 0, true);
			    p.getPackets().sendIComponentText(3000, 5, "");	
				}
            }
        }, 0, 1);
    }
    
    public static String getTime(Player player) {
        if (player.overloadCount == 0) {
        	player.overloadMin--;
        	player.overloadCount = 60;
        }
        if (player.overloadMin == 0 && player.overloadCount == 0) {
        	player.getPackets().sendHideIComponent(3000, 0, true);
			player.getPackets().sendIComponentText(3000, 5, "");
        }
        return player.overloadMin+"m "+player.overloadCount+ "s";
    }
}