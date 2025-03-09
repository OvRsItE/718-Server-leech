package com.rs.game.world.entity.npc.dungeonnering;


import com.rs.game.map.Position;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.player.content.skills.dungeoneering.RoomReference;

@SuppressWarnings("serial")
public class ToKashBloodChiller extends DungeonBoss {

	private boolean specialAttack;

	public ToKashBloodChiller(int id, Position tile, DungeonManager manager, RoomReference reference) {
		super(id, tile, manager, reference);
	}

	@Override
	public double getMeleePrayerMultiplier() {
		return 1.5;
	}

	@Override
	public double getRangePrayerMultiplier() {
		return 1.5;
	}

	public void setSpecialAttack(boolean specialAttack) {
		this.specialAttack = specialAttack;
	}

	public boolean canSpecialAttack() {
		return !specialAttack;
	}
}
