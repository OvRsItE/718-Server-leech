package com.rs.game.world.entity.player.content;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;

/**
 * 
 * @author JazzyYaYaYa | Nexon | Fuzen Seth
1648 - cranking wheel ectophuntus
1649 - putting bones into machine
1651 - ectophuntus pray emote
1652 - shake vial
 */
public class Ectophial {

	Player player;
	
	public Ectophial(Player player) {
		this.player = player;
	}

	private int FULL_ECTOPHIAL = 4251, EMPTY_ECTOPHIAL = 4252;
	/**
	 * Teleport Landing Coords
	 */
	public final int LANDING_X = 3659;
	public final int LANDING_Y = 3517;

	/**
	 *  This is for Worship, this will refill ectohpial
	 * @param player
	 */
	public void refillEctophial(Player player) {
	if (player.getInventory().containsItem(EMPTY_ECTOPHIAL, 1)) {
		player.lock(2);
		player.getInventory().deleteItem(EMPTY_ECTOPHIAL,1);
		player.sendMessage("You refill the ectophial.");
		player.animate(new Animation (1649));
		player.getInventory().addItem(FULL_ECTOPHIAL,1);
	} else {
		player.sendMessage("You cannot worship ectofuntus.");
	}
}
	
	public void ProcessTeleportation(final Player player) {
			WorldTasksManager.schedule(new WorldTask() {
			int loop;
			
			@Override
			public void run() {
				player.getInterfaceManager().closeChatBoxInterface();			
				if (loop == 0) {
                    player.animate(new Animation(9609));
                    player.setNextGraphics(new Graphics(1688));
				} else if (loop == 2) {
					player.getInventory().deleteItem(FULL_ECTOPHIAL,1);
                    player.animate(new Animation(8939));
                    player.setNextGraphics(new Graphics(1678));
				} else if (loop == 4) {
					player.getInventory().addItem(EMPTY_ECTOPHIAL,1);
					player.setNextPosition(new Position(LANDING_X, LANDING_Y, 0));
				player.animate(new Animation(8941));
				player.setNextGraphics(new Graphics(1679));
				player.closeInterfaces();
				stop();
				}
				loop++;
				}
			}, 0, 1);
		}
	
}