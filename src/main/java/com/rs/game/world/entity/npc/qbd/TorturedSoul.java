package com.rs.game.world.entity.npc.qbd;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

/**
 * Represents a Tortured soul.
 * @author Emperor
 *
 */
public final class TorturedSoul extends NPC {

	/**
	 * The serial UID.
	 */
	private static final long serialVersionUID = -1148744797884976406L;

	/**
	 * The messages the NPC can say.
	 */
	private static final ForceTalk[] FORCE_MESSAGES = {
		new ForceTalk("NO MORE! RELEASE ME, MY QUEEN! I BEG YOU!"),
		new ForceTalk("We lost our free will long ago..."),
		new ForceTalk("How long has it been since I was taken..."),
		new ForceTalk("The cycle is never ending, mortal...")
	};
	
	/**
	 * The teleport graphic.
	 */
	static final Graphics TELEPORT_GRAPHIC = new Graphics(3147);
	
	/**
	 * The teleport animation.
	 */
	static final Animation TELEPORT_ANIMATION = new Animation(16861);
	
	/**
	 * The special attack graphic.
	 */
	private static final Graphics SPECIAL_GRAPHIC = new Graphics(3146);
	
	/**
	 * The special attack graphic.
	 */
	private static final Graphics SPECIAL_ATT_GFX_ = new Graphics(3145);
	
	/**
	 * The special attack animation.
	 */
	private static final Animation SPECIAL_ATT_ANIM_ = new Animation(16864);
	
	/**
	 * The queen black dragon reference.
	 */
	private final QueenBlackDragon dragon;
	
	/**
	 * The player victim.
	 */
	private final Player victim;
	
	/**
	 * If the NPC should skip a walk step.
	 */
	private boolean skipWalkStep = true;
	
	/**
	 * Constructs a new {@code TorturedSoul} {@code Object}.
	 * @param dragon The queen black dragon reference.
	 * @param victim The player victim.
	 * @param spawn The spawn location.
	 */
	public TorturedSoul(QueenBlackDragon dragon, Player victim, Position spawn) {
		super(15510, spawn, -1, 0, true, false);
		super.setForceMultiArea(true);
		this.dragon = dragon;
		this.victim = victim;
		super.setRandomWalk(false);
		super.getCombat().setTarget(victim);
	}

	/**
	 * Switches the walk step value and returns it.
	 * @return {@code True} if the npc should skip one movement tick.
	 */
	public boolean switchWalkStep() {
		return skipWalkStep = !skipWalkStep;
	}
	
	@Override
	public void sendDeath(Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		getCombat().removeTarget();
		animate(null);
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					animate(new Animation(defs.getDeathEmote()));
				} else if (loop >= defs.getDeathDelay()) {
					finish();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}
	
	/**
	 * Sends the special attack.
	 */
	public void specialAttack(Position teleport) {
		super.getCombat().addCombatDelay(10);
		super.setNextPosition(teleport);
		super.setNextGraphics(TELEPORT_GRAPHIC);
		super.animate(TELEPORT_ANIMATION);
		super.getCombat().reset();
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				stop();
				int diffX = getX() - victim.getX(), diffY = getY() - victim.getY();
				if (diffX < 0) {
					diffX = -diffX;
				}
				if (diffY < 0) {
					diffY = -diffY;
				}
				int offsetX = 0, offsetY = 0;
				if (diffX > diffY) {
					offsetX = getX() - victim.getX() < 0 ? -1 : 1;
				} else {
					offsetY = getY() - victim.getY() < 0 ? -1 : 1;
				}
				if (victim.transform(offsetX, offsetY, 0).matches(TorturedSoul.this)) {
					offsetX = -offsetX;
					offsetY = -offsetY;
				}
				final int currentX = offsetX + victim.getX();
				final int currentY = offsetY + victim.getY();
				setNextForceTalk(FORCE_MESSAGES[Utils.random(FORCE_MESSAGES.length)]);
				setNextGraphics(SPECIAL_ATT_GFX_);
				animate(SPECIAL_ATT_ANIM_);
				getCombat().setTarget(victim);
				WorldTasksManager.schedule(new WorldTask() {
					int x = currentX, y = currentY;
					@Override
					public void run() {
						Position current = new Position(x, y, 1);
						victim.getPackets().sendGraphics(SPECIAL_GRAPHIC, current);
						Entity target = null;
						for (TorturedSoul soul : dragon.getSouls()) {
							if (soul.matches(current)) {
								target = soul;
								break;
							}
						}
						if (target == null) {
							for (NPC worm : dragon.getWorms()) {
								if (worm.matches(current)) {
									target = worm;
									break;
								}
							}
						}
						if (target == null && victim.matches(current)) {
							target = victim;
						}
						if (target != null) {
							stop();
							target.applyHit(new Hit(dragon, Utils.random(200, 260), HitLook.REGULAR_DAMAGE));
							return;
						}
						if (x > victim.getX()) {
							x--;
						} else if (x < victim.getX()) {
							x++;
						}
						if (y > victim.getY()) {
							y--;
						} else if (y < victim.getY()) {
							y++;
						}
					}
				}, 0, 0);
			}
		}, 1);
	}
}