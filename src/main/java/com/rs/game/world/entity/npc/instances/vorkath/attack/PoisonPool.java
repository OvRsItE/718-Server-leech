/**
 * 
 */
package com.rs.game.world.entity.npc.instances.vorkath.attack;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.instances.vorkath.VorkathNPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

/**
 * @author ReverendDread
 * Sep 20, 2018
 */
public class PoisonPool implements VorkathAttack {

	private static final Projectile AFTER_POISON_PROJ = new Projectile(Graphics.createOSRS(1482).getId(), 25, 25, 0, 15, 58, 0);
	private static final Graphics AFTER_POISON_PROJ_GFX = Graphics.createOSRS(131, 0, 92);
	
	private static final Animation POISON_POOL_ATTACK_ANIM = Animation.createOSRS(7960);
	private static final Projectile POISON_POOL_PROJ = new Projectile(Graphics.createOSRS(1483).getId(), 25, 0, 35, 15, 58, 0);
	
	private static final Animation RAPID_FIRE_ANIM = Animation.createOSRS(7960);
	
	private static final Position BOTTOM_LEFT = new Position(2261, 4054, 0);
	private static final Position TOP_RIGHT = new Position(2283, 4076, 0);
	
	/* (non-Javadoc)
	 * @see com.rs.game.world.entity.npc.instances.vorkath.attack.VorkathAttack#attack(com.rs.game.world.entity.npc.instances.vorkath.VorkathNPC, com.rs.game.world.entity.player.Player)
	 */
	@Override
	public int attack(VorkathNPC vorkath, Player target) {	
		vorkath.animate(POISON_POOL_ATTACK_ANIM);
		ArrayList<Position> tiles = getRandomTiles(vorkath);
		tiles.forEach((tile) -> {
			World.sendProjectile(vorkath.getMiddleTile(), vorkath.getInstance().getLocation(tile), POISON_POOL_PROJ);
			CoresManager.slowExecutor.schedule(() -> {
				addPool(vorkath, vorkath.getInstance().getLocation(tile));
			}, 1200, TimeUnit.MILLISECONDS);
		});
		WorldTasksManager.schedule(new WorldTask() {
			int ticks = 0;		
			@Override
			public void run() {
				if (ticks <= 25 && vorkath.getId() != 28059) {
					if (onPoison(tiles, vorkath, target)) {
						int damage = Utils.random(100);
						target.applyHit(new Hit(vorkath, damage, HitLook.REGULAR_DAMAGE));
						vorkath.applyHit(new Hit(target, damage, HitLook.HEALED_DAMAGE));
					}
					final Position position = target.getPosition();
					vorkath.animate(RAPID_FIRE_ANIM);
					World.sendProjectile(vorkath.getMiddleTile(), position, AFTER_POISON_PROJ);
					CoresManager.slowExecutor.schedule(() -> {
						World.sendGraphics(vorkath, AFTER_POISON_PROJ_GFX, position);
						if (target.getPosition().matches(position)) {
							target.applyHit(new Hit(vorkath, Utils.random(300, 400), HitLook.REGULAR_DAMAGE));
						}
					}, 600, TimeUnit.MILLISECONDS);
				} else {
					stop();
				}
				ticks++;
			}
			
		}, 1, 0);	
		return 30;
	}
	
	private void addPool(VorkathNPC vorkath, Position tile) {
		WorldObject object = new WorldObject(WorldObject.asOSRS(32000), 10, Utils.random(4), tile.getX(), tile.getY(), tile.getZ());
		World.spawnObject(object, true);
		WorldTasksManager.schedule(new WorldTask() {

			int ticks = 0;
			
			@Override
			public void run() {
				if (ticks <= 10 && vorkath.getId() != NPC.asOSRS(8059)) {
					if (vorkath.isDead() || vorkath.isFinished()) {
						World.removeObject(object, true);
						stop();
					}
					ticks++;
				} else {
					World.removeObject(object, true);
					stop();
				}
			}
			
		}, 0, 1);
	}
	
	/**
	 * Gets random tiles in vorkaths arena.
	 * @return
	 */
	public ArrayList<Position> getRandomTiles(VorkathNPC vorkath) {
		ArrayList<Position> tiles = new ArrayList<Position>();
		for (int x = BOTTOM_LEFT.getX(); x < TOP_RIGHT.getX(); x++) {
			for (int y = BOTTOM_LEFT.getY(); y < TOP_RIGHT.getY(); y++) {
				if (World.getMask(0, x, y) != 0 || (x > vorkath.getInstance().getLocation(2268, 4061, 0).getX() 
						&& x < vorkath.getInstance().getLocation(2276, 4069, 0).getX()) 
						|| (y > vorkath.getInstance().getLocation(2268, 4061, 0).getY() 
								&& y < vorkath.getInstance().getLocation(2276, 4069, 0).getY()))
					continue;
				boolean choosen = Utils.random(3) == 1;
				if (choosen)
					tiles.add(new Position(x, y, vorkath.getZ()));
			}
		}
		return tiles;
	}
	
	/**
	 * Checks if the player is on a poison tile.
	 * @param tiles
	 * 			list of tiles that have poison on them.
	 * @param vorkath TODO
	 * @param player
	 * 			the player.
	 * @return
	 * 			if the player is on a poison tile.
	 */
	public boolean onPoison(ArrayList<Position> tiles, VorkathNPC vorkath, Player player) {
		for (Position tile : tiles) {
			if (player.getPosition().matches(vorkath.getInstance().getLocation(tile)))
				return true;
		}
		return false;
	}

}
