package com.rs.game.world.entity.player.content; 
  
import com.rs.game.world.entity.player.Player;
  
/** 
 *  
 * @author Xiles 
 * 
 */
public class SecuritySystem { 
     
   
    public static void checkStaff(Player player) { 
    	if (!player.hasStaffPin) {
   			player.getTemporaryAttributtes().put("banning_security", Boolean.TRUE);
			player.getPackets().sendRunScript(108, new Object[] { "Please enter your security pin"});
    	}
    }
} 