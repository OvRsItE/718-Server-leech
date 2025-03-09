package com.rs.game.world.entity.npc.qbd;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.BuffManager.BuffType;
import com.rs.game.world.entity.player.content.Combat;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

/**
 * Represents a default fire breath attack.
 * @author Emperor
 *
 */
public final class FireBreathAttack implements QueenAttack {

	/**
	 * The animation of the attack.
	 */
	private static final Animation ANIMATION = new Animation(16721);
	
	/**
	 * The graphic of the attack.
	 */
	private static final Graphics GRAPHIC = new Graphics(3143);
	
	@Override
	public int attack(final QueenBlackDragon npc, final Player victim) {
		npc.animate(ANIMATION);
		npc.setNextGraphics(GRAPHIC);
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				super.stop();
				String message = getProtectMessage(victim);
				int hit;
				if (message != null && !(victim.getSuperFireImmune() > Utils.currentTimeMillis())) {
					hit = Utils.random(60 + Utils.random(150), message.contains("prayer") ? 460 : 235);
					victim.getPackets().sendGameMessage(message);
				} else if (victim.getBuffManager().hasBuff(BuffType.SUPER_ANTI_FIRE)) {
					victim.getPackets().sendGameMessage("Your potion absorbs the dragon's breath!");
					hit = 0;
				} else {
					hit = Utils.random(400, 710);
					victim.getPackets().sendGameMessage("You are horribly burned by the dragon's breath!");
				}
				victim.animate(new Animation(Combat.getDefenceEmote(victim)));
				victim.applyHit(new Hit(npc, hit, HitLook.REGULAR_DAMAGE));
			}		
		}, 1);
		return Utils.random(4, 15); //Attack delay seems to be random a lot.
	}

	@Override
	public boolean canAttack(QueenBlackDragon npc, Player victim) {
		return true;
	}

	/**
	 * Gets the dragonfire protect message.
	 * @param player The player.
	 * @return The message to send, or {@code null} if the player was unprotected.
	 */
	public static final String getProtectMessage(Player player) {
		if (Combat.hasAntiDragProtection(player) || player.getBuffManager().hasBuff(BuffType.SUPER_ANTI_FIRE)) {
			return "Your shield absorbs most of the dragon's breath!";
		}
		if (player.getBuffManager().hasBuff(BuffType.ANTI_FIRE) || player.getBuffManager().hasBuff(BuffType.SUPER_ANTI_FIRE)) {
			return "Your potion absorbs most of the dragon's breath!";
		} else if (player.getPrayer().usingPrayer(0, 17) || player.getPrayer().usingPrayer(1, 7)) {
			return "Your prayer absorbs most of the dragon's breath!";
		}
		return null;
	}
}