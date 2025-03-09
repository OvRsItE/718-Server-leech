package com.rs.game.world.entity.npc.alchemist;

import java.util.ArrayList;

import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.familiar.Familiar;
import com.rs.game.world.entity.updating.impl.CombatStyle;
import com.rs.game.world.entity.updating.impl.Hit;
import com.rs.game.world.entity.updating.impl.Hit.HitLook;

@SuppressWarnings("serial")
public class TheAlchemist extends NPC {

	private CombatStyle style;
	
	private byte phase;
	
	private boolean reflecting;
	
	private ArrayList<NPC> minions = new ArrayList<NPC>();
	
	public TheAlchemist(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, double enrage) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		switchStyles(CombatStyle.MELEE);
		setCanBeFrozen(false);
		setDamageCap(500);
	}

	@Override
	public void sendDeath(final Entity source) {
		for (NPC minion : minions) {
			if (!minion.isDead())
			minion.sendDeath(source);
		}
		super.sendDeath(source);
	}
	
	@Override
	public void finish() {
		for (NPC minion : minions) {
			if (!minion.isDead() || !minion.isFinished())
			minion.finish();
		}
		super.finish();
	}
	
	@Override
	public void handleIngoingHit(final Hit hit) {
		if (hit.getLook() != HitLook.MELEE_DAMAGE && hit.getLook() != HitLook.RANGE_DAMAGE && hit.getLook() != HitLook.MAGIC_DAMAGE)
			return;
		if (hit.getSource() instanceof Familiar) {
			applyHit(new Hit(hit.getSource(), hit.getDamage(), HitLook.HEALED_DAMAGE));
			hit.getSource().applyHit(new Hit(hit.getSource(), hit.getDamage(), HitLook.REGULAR_DAMAGE));
			hit.setDamage(0);
		} else if (reflecting && hit.getSource() != null) {
			hit.getSource().applyHit(new Hit(hit.getSource(), (int) (hit.getDamage() * 0.30), HitLook.REFLECTED_DAMAGE));	
		} else if (phase == 3 && (getHitpoints() <= getMaxHitpoints() * 0.50) && !reflecting) {
			reflecting = true;
		} else if ((getHitpoints() <= getMaxHitpoints() * 0.25) && phase == 2) { //25% hitpoints all styles
			switchStyles(CombatStyle.ALL);
			maxHeal();
			phase = 3;
		} else if ((getHitpoints() <= getMaxHitpoints() * 0.25) && phase == 1) { //25% hitpoints range phase
			switchStyles(CombatStyle.RANGE);	
			maxHeal();
			phase = 2;
		} else if (getHitpoints() <= getMaxHitpoints() * 0.25 && phase == 0) { //25% hitpoints magic phase
			switchStyles(CombatStyle.MAGIC);
			maxHeal();
			phase = 1;
		}
		super.handleIngoingHit(hit);
	}
	
	@Override
	public double getRangePrayerMultiplier() {
		return 0.5;
	}
	
	@Override
	public double getMagePrayerMultiplier() {
		return 0.5;
	}
	
	@Override
	public double getMeleePrayerMultiplier() {
		return 0.5;
	}
	
	private void maxHeal() {
		heal(getMaxHitpoints() - getHitpoints());
	}
	
	private void switchStyles(CombatStyle style) {
		this.style = style;
		switch (style) {
			case MAGIC:
				transformIntoNPC(16071);
				setForceFollowClose(false);
				break;
			case MELEE:
				transformIntoNPC(16080);
				setForceFollowClose(true);
				break;
			case RANGE:
				transformIntoNPC(16081);
				setForceFollowClose(false);
				break;
			case ALL:
				setBonuses(new int[] { 2000, 2000, 2000, 2000, 2000, 500, 500, 500, 500, 500});
				break;
		}		
	}
	
	public NPC addMinion(NPC minion) {
		minions.add(minion);
		return minion;
	}
	
	public CombatStyle getCombatStyle() {
		return style;
	}

	public ArrayList<NPC> getMinions() {
		return minions;
	}
	
	public byte getPhase() {
		return phase;
	}
	
}
