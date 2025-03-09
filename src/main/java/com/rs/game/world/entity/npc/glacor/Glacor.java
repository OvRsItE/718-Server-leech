package com.rs.game.world.entity.npc.glacor;

import java.util.ArrayList;
import java.util.List;

import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.updating.impl.Hit;

@SuppressWarnings("serial")
public class Glacor extends Glacyte {

	private List<Glacyte> glacites;
	private boolean rangeAttack;

	public Glacor(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(null, id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		setDamageCap(2500);
		setEffect((byte) -1);
		setGlacor(this);
		setSpawned(false);
		canBeAttackFromOutOfArea();
		setNoDistanceCheck(false);

	}

	@Override
	public void handleIngoingHit(final Hit hit) {
		if (glacites == null) {
			if (getHitpoints() <= getMaxHitpoints() / 2) {
				glacites = new ArrayList<Glacyte>(2);
				createGlacites();
			}
		} else if (glacites.size() != 0) {
			hit.setDamage(0);
		}
		super.handleIngoingHit(hit);
	}

	private void createGlacites() {
		for (int index = 0; index < 3; index++) {
			Position tile = new Position(this, 2);
			glacites.add(new Glacyte(this, 14302 + index, tile, -1, true));
		}
	}

	public void verifyGlaciteEffect(Glacyte glacite) {
		if (glacites.size() == 1) {
			setEffect(glacites.get(0).getEffect());
		}
		glacites.remove(glacite);
	}

	@Override
	public void sendDeath(Entity killer) {
		super.sendDeath(killer);
		glacites = null;
	}

	public void setRangeAttack(boolean rangeAttack) {
		this.rangeAttack = rangeAttack;
	}

	public boolean isRangeAttack() {
		return rangeAttack;
	}

	public void resetMinions() {
		glacites = null;
		setEffect((byte) -1);
	}
}
