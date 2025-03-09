/**
 * 
 */
package com.rs.game.world.entity.npc.instances.vorkath.attack;

import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.map.Position;
import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.instances.vorkath.VorkathNPC;
import com.rs.game.world.entity.npc.instances.vorkath.ZombifiedSpawn;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

/**
 * @author ReverendDread
 * Sep 20, 2018
 */
public class IceDragonfire implements VorkathAttack {

	private static final Animation BASIC_ATTACK_ANIM = Animation.createOSRS(7952);
	private static final Graphics ICE_BARRAGE_GFX = Graphics.createOSRS(369);
	private static final Projectile ICE_DRAGONFIRE_PROJ = new Projectile(Graphics.createOSRS(395).getId(), 30, 25, 27, 15, 30, 0);
	private static final Projectile SPAWN_PROJ = new Projectile(Graphics.createOSRS(1484).getId(), 30, 25, 27, 15, 30, 0);
	
	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.npc.instances.vorkath.attack.VorkathAttack#attack(com.rs.game.world.entity.npc.instances.vorkath.VorkathNPC, com.rs.game.world.entity.player.Player)
	 */
	@Override
	public int attack(VorkathNPC vorkath, Player target) {
		World.sendProjectile(vorkath, target, ICE_DRAGONFIRE_PROJ);
		vorkath.animate(BASIC_ATTACK_ANIM);
		Position freeTile = getFreeTile(vorkath, target);
		World.sendProjectile(vorkath.getMiddleTile(), freeTile, SPAWN_PROJ);
		CoresManager.slowExecutor.schedule(() -> {
			target.setNextGraphics(ICE_BARRAGE_GFX);
			ZombifiedSpawn minion = new ZombifiedSpawn(NPC.asOSRS(8062), freeTile, target);
			int freeze = Utils.getDistance(minion, target);
			target.addFreezeDelay(freeze * 1000);
			vorkath.setMinion(minion);
		}, (ICE_DRAGONFIRE_PROJ.getHitSyncToMillis(vorkath.getMiddleTile(), freeTile) + 600), TimeUnit.MILLISECONDS);
		return 17;
	}
	
	/**
	 * Gets a free tile for the zombified spawn to spawn on.
	 * @param vorkath
	 * 			the player.
	 * @return
	 */
	public Position getFreeTile(VorkathNPC vorkath, Player player) {
		Position destination = null;
		for (int index = 0; index < 10; index++) {
			destination = new Position(vorkath, 6, 10);
			if (World.getMask(destination.getZ(), destination.getX(),
					destination.getY()) == 0 && !destination.withinDistance(player, 6) && destination.withinDistance(player, 10)) {
				return destination;
			}
		}
		if (World.getMask(destination.getZ(), destination.getX(),
				destination.getY()) == 0 && !destination.withinDistance(player, 6) && destination.withinDistance(player, 10)) {
			return getFreeTile(vorkath, player);
		}
		return destination;
	}

}
