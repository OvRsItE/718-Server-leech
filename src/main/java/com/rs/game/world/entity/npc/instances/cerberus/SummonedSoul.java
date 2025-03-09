/**
 * 
 */
package com.rs.game.world.entity.npc.instances.cerberus;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.Projectile;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Misc;
import com.rs.utility.Utils;

/**
 * Represents a summoned soul from cerberus.
 * @author ReverendDread
 * 4485
 * Sep 21, 2018
 */
public class SummonedSoul extends NPC {

	private static final long serialVersionUID = -6461396296539258205L;
	
	private Player player;
	
	private CerberusNPC cerberus;
	
	private boolean attacked;
	
	private static final Projectile MAGIC_PROJ = new Projectile(5126, 30, 25, 27, 15, 30, 0);
	
	private static final Projectile RANGED_PROJ = new Projectile(5027, 30, 25, 27, 15, 30, 0);
	
	private static final Projectile MELEE_PROJ = new Projectile(6248, 30, 25, 27, 15, 30, 0);
	
	private static final String[] QUOTES = new String[] { "Join us.", "I obey.", "Steal your soul." };
	
	private final HitLook hitLook = (getId() == CerberusNPC.MAGIC ? HitLook.MAGIC_DAMAGE : getId() == CerberusNPC.MELEE ? HitLook.MELEE_DAMAGE : HitLook.RANGE_DAMAGE);
	
	private final Projectile projectile = (getId() == CerberusNPC.MAGIC ? MAGIC_PROJ : getId() == CerberusNPC.MELEE ? MELEE_PROJ : RANGED_PROJ);
	
	/**
	 * @param cerberus
	 * @param player
	 * @param id
	 * @param tile
	 * @param mapAreaNameHash
	 * @param canBeAttackFromOutOfArea
	 * @param spawned
	 */
	public SummonedSoul(CerberusNPC cerberus, Player player, int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
		addWalkSteps(tile.getX(), tile.getY() - 10, 10, false);
		setNextForceTalk(new ForceTalk("Aaarrrooooooo."));
		World.sendGraphics(this, Graphics.createOSRS(1247), tile);
		setCantInteract(true);
		setForceMultiArea(true);
		setCanBeFrozen(false);
		this.player = player;
		this.cerberus = cerberus;
	}
	
	@Override
	public void processNPC() {
		super.processNPC();
		final SummonedSoul soul = this;
		if (player.isDead() || player.isFinished())
			return;
		if (getId() == cerberus.getMinions()[0].getId() && cerberus.stage == 0 && !cerberus.getMinions()[0].attacked && (!hasWalkSteps())) {
			if (cerberus.isDead()) {
				finish();
				return;
			}
			WorldTasksManager.schedule(new WorldTask() {

				int ticks = 0;
				boolean prayer;
				
				@Override
				public void run() {
					if (ticks == 0) {
						soul.faceEntity(player);
						World.sendProjectile(soul, player, projectile);	
						animate(Animation.createOSRS(getAttackEmote(hitLook)));
						prayer = player.getPrayer().isProtectingFrom(hitLook);
					} else if (ticks == 1) {
						if (!prayer)
							player.applyHit(new Hit(cerberus, 320, hitLook));
						else 
							player.getPrayer().drainPrayer(player.getEquipment().getShieldId() == 13744 ? 150: 300);
					} else if (ticks == 2) {
						cerberus.stage++;
					} else if (ticks == 3) {
						soul.addWalkSteps(getX(), getY() + 10, 10, false);
					} else if (ticks == 13) {
						soul.setNextForceTalk(new ForceTalk(getRandomQuote()));
					} else if (ticks == 17) {
						soul.finish();
						stop();
					}
					ticks++;
				}
				
			}, 0, 1);
			cerberus.getMinions()[0].attacked = true;
		}
		if (getId() == cerberus.getMinions()[1].getId() && cerberus.stage == 1 && !cerberus.getMinions()[1].attacked && !(hasWalkSteps())) {
			if (cerberus.isDead()) {
				finish();
				return;
			}
			WorldTasksManager.schedule(new WorldTask() {

				int ticks = 0;
				boolean prayer;
				
				@Override
				public void run() {
					if (ticks == 0) {
						soul.faceEntity(player);
						animate(Animation.createOSRS(getAttackEmote(hitLook)));
						World.sendProjectile(soul, player, projectile);
						prayer = player.getPrayer().isProtectingFrom(hitLook);
					} else if (ticks == 1) {
						if (!prayer)
							player.applyHit(new Hit(cerberus, 320, hitLook));
						else 
							player.getPrayer().drainPrayer(player.getEquipment().getShieldId() == 13744 ? 150: 300);
					} else if (ticks == 2) {
						cerberus.stage++;
					} else if (ticks == 3) {
						soul.addWalkSteps(getX(), getY() + 10, 10, false);
					} else if (ticks == 13) {
						soul.setNextForceTalk(new ForceTalk(getRandomQuote()));
					} else if (ticks == 17) {
						soul.finish();
						stop();
					}
					ticks++;
				}
				
			}, 0, 1);
			cerberus.getMinions()[1].attacked = true;
		}
		if (getId() == cerberus.getMinions()[2].getId() && cerberus.stage == 2 && !cerberus.getMinions()[2].attacked && !(hasWalkSteps())) {
			if (cerberus.isDead()) {
				finish();
				return;
			}
			WorldTasksManager.schedule(new WorldTask() {

				int ticks = 0;
				boolean prayer;
				
				@Override
				public void run() {
					if (ticks == 0) {
						soul.faceEntity(player);
						animate(Animation.createOSRS(getAttackEmote(hitLook)));
						World.sendProjectile(soul, player, projectile);
						prayer = player.getPrayer().isProtectingFrom(hitLook);
					} else if (ticks == 1) {
						if (!prayer)
							player.applyHit(new Hit(cerberus, 320, hitLook));
						else 
							player.getPrayer().drainPrayer(player.getEquipment().getShieldId() == 13744 ? 150: 300);
					} else if (ticks == 2) {
						cerberus.stage++;
					} else if (ticks == 3) {
						soul.addWalkSteps(getX(), getY() + 10, 10, false);
					} else if (ticks == 13) {
						soul.setNextForceTalk(new ForceTalk(getRandomQuote()));
					} else if (ticks == 17) {
						soul.finish();
						stop();
					}
					ticks++;
				}
				
			}, 0, 1);
			cerberus.getMinions()[2].attacked = true;
		}
	}

	public String getRandomQuote() {
		return QUOTES[Utils.random(QUOTES.length - 1)];
	}
	
	public int getAttackEmote(HitLook look) {
		if (look == HitLook.MELEE_DAMAGE)
			return 8528;
		if (look == HitLook.RANGE_DAMAGE)
			return 8529;
		return 8530;
	}
	
}
