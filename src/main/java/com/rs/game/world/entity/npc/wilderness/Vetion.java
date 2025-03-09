package com.rs.game.world.entity.npc.wilderness;

import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.ForceTalk;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;
import com.rs.utility.Utils;

@SuppressWarnings("serial")
public class Vetion extends NPC {

	// EARTHQUAKE ANIM - 5507
	
	NPC[] minions;
	
	private boolean[] spawned_minions = new boolean[2];
	
	public Vetion(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		setForceMultiArea(true);
	}
	
	@Override
	public void handleIngoingHit(final Hit hit) {
		int random = Utils.random(0, 10);
		if (random == 0 && hit.getSource() != null)
			earthquake((Player) hit.getSource());
		if (getHitpoints() <= (getMaxHitpoints() / 2) && 
				getId() == 26611 && !spawned_minions[0]) {
			spawnMinions(0);
		}
		if (getHitpoints() <= (getMaxHitpoints() / 2) && 
				getId() == 26612 && !spawned_minions[1]) {
			spawnMinions(1);
		}
		if (!minions_dead())
			hit.setDamage(0);
		super.handleIngoingHit(hit);
	}

	@Override
	public void sendDeath(Entity source) {
		if (this.getId() == 26611) {
			transformIntoNPC(26612);
			setHitpoints(getMaxHitpoints());
			setNextForceTalk(new ForceTalk("Do it again!"));
			return;
		}
		super.sendDeath(source);
	}
	
	@Override
	public void finish() {
		transformIntoNPC(26611);
		super.finish();
	}
	
	@Override
	public double getMeleePrayerMultiplier() {
		return 0.2;
	}
	
	@Override
	public double getMagePrayerMultiplier() {
		return 1.0;
	}
	
	/**
	 * Checks if Vet'ions minions are dead or not.
	 * @return
	 */
	private boolean minions_dead() {
		if (minions == null)
			return true;
		if (minions[0].isFinished() && minions[1].isFinished())
			return true;
		return false;
	}
	
	/**
	 * Handles Vet'ion's earthquake attack.
	 * @param target
	 */
	private void earthquake(Player target) {
		animate(new Animation(5507));
		if (Utils.getDistance(this, target) <= 11) {
			target.applyHit(new Hit(this, 250 + Utils.random(150), HitLook.REGULAR_DAMAGE));
			target.getPackets().sendGameMessage("Vet'ion pummels the ground sending a shattering earthquake shockwave through you.", true);
		}			
	}
	
	/**
	 * Handles Vet'ion's minion spawning.
	 */
	private void spawnMinions(int phase) {
		minions = new NPC[2];
		minions[0] = new NPC(1575, new Position(getX(), getY() + 2, getZ()), -1, 0, true, true);
		minions[1] = new NPC(1575, new Position(getX(), getY() - 2, getZ()), -1, 0, true, true);
		setNextForceTalk(new ForceTalk(Utils.random(1) == 0 ? "Kill, my pets!" : "Bahh! Go, dogs!!"));
		minions[0].setNextForceTalk(new ForceTalk("GRRRRRRRRRRRR"));
		minions[1].setNextForceTalk(new ForceTalk("GRRRRRRRRRRRR"));
		if (getCombat().getTarget() != null) {
			minions[0].getCombat().setTarget(getCombat().getTarget());
			minions[1].getCombat().setTarget(getCombat().getTarget());
		}
		spawned_minions[phase] = true;
	}

}
