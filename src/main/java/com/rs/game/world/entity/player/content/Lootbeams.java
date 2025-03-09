/**
 * 
 */
package com.rs.game.world.entity.player.content;

import com.rs.cores.CoresManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Graphics;

import lombok.Getter;

import java.util.concurrent.TimeUnit;

/**
 * Handles lootbeam related things.
 * @author ReverendDread
 * Dec 8, 2018
 */
public class Lootbeams {

	public enum Tier {
		

		SMALL(4048),
		MEDIUM(4049),
		LARGE(2),
		HUGE(2);

		

			
		/** The gfx id */
		@Getter private int gfx;
		
		/**
		 * Constructor
		 * @param gfx
		 * 			the gfx id.
		 */
		private Tier(int gfx) {
			this.gfx = gfx;
		}
		
	}
	
	/**
	 * Creates a loot beam on the desired {@code WorldTile}.
	 * @param tier
	 * 			the tier of loot beam.
	 * @param duration
	 * 			the duration.
	 */
	public static void create(Position tile, Player player, Tier tier) {
		player.getPackets().sendGraphics(new Graphics(tier.getGfx()), tile);
		CoresManager.slowExecutor.schedule(new Runnable() {

			@Override
			public void run() {
				player.getPackets().sendGraphics(new Graphics(-1), tile);
			}

		}, 180, TimeUnit.SECONDS);
	    player.sm("<col=E89002>A golden beam shines over one of your items.<col/>");
	}
	
}
