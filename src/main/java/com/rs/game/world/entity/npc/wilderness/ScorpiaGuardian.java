/**
 * 
 */
package com.rs.game.world.entity.npc.wilderness;

import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

import lombok.Getter;

/**
 * @author ReverendDread
 * Jul 26, 2018
 */
@SuppressWarnings("serial")
public class ScorpiaGuardian extends NPC {
	
	private int lastAttack;
	
	/** The owner of this guardian */
	@Getter private Scorpia scorpia;
	
	/**
	 * @param id
	 * @param tile
	 * @param mapAreaNameHash
	 * @param canBeAttackFromOutOfArea
	 * @param spawned
	 */
	public ScorpiaGuardian(Scorpia scorpia, Position tile) {
		super(16035, tile, -1, 0, true, true);
		this.scorpia = scorpia;
		setAtMultiArea(true);
		setForceMultiArea(true);
		setFollowing(scorpia);
		getCombat().setCombatDelay(Integer.MAX_VALUE);
	}
	
	@Override
	public void processNPC() {
		super.processNPC();
		handleCombat();
	}
	
	private final void handleCombat() {
		if (lastAttack >= 4) {
			animate(getCombatDefinitions().getAttackEmote());
			World.sendProjectile(this, getScorpia(), 143, 10, 30, 40, 10, 16, 0);
			getScorpia().applyHit(new Hit(getScorpia(), Utils.random(10, 40), HitLook.HEALED_DAMAGE, 1));
			CoresManager.slowExecutor.schedule(() -> {
				getScorpia().setNextGraphics(144);
			}, 600, TimeUnit.MILLISECONDS);
			lastAttack = 0;
		}
		lastAttack++;
	}

}
