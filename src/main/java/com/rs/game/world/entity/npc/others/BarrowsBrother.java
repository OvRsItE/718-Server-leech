package com.rs.game.world.entity.npc.others;

import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.controller.impl.Barrows;
import com.rs.utility.Utils;

@SuppressWarnings("serial")
public class BarrowsBrother extends NPC {

	private Barrows barrows;

	public BarrowsBrother(int id, Position tile, Barrows barrows) {
		super(id, tile, -1, 0, true, true);
		this.barrows = barrows;
	}

	@Override
	public void sendDeath(Entity source) {
		if(barrows != null) {
			barrows.targetDied();
			barrows = null;
		}
		super.sendDeath(source);
	}
	
	@Override
	public double getMeleePrayerMultiplier() {
		return getId() != 2030 ? 0 : Utils.random(3) == 0 ? 1 : 0;
	}
	
	
	public void disapear() {
		barrows = null;
		finish();
	}
	@Override
	public void finish() {
		if(isFinished())
			return;
		if(barrows != null) {
			barrows.targetFinishedWithoutDie();
			barrows = null;
		}
		super.finish();
	}

}
