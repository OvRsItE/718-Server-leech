package com.rs.game.world.entity.npc.dungeonnering;


import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;

@SuppressWarnings("serial")
public class TombOfLexicus extends DungeonNPC {

	private LexicusRunewright boss;

	public TombOfLexicus(LexicusRunewright boss, int id, Position tile, DungeonManager manager, double multiplier) {
		super(id, tile, manager, multiplier);
		setForceAgressive(true);
		this.boss = boss;
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public double getMagePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return 0.6;
	}

	@Override
	public void drop() {

	}

	@Override
	public int getMaxHitpoints() {
		return 10;
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		boss.removeBook(this);
	}
}
