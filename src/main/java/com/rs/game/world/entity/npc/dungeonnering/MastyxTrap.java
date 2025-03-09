package com.rs.game.world.entity.npc.dungeonnering;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;

@SuppressWarnings("serial")
public class MastyxTrap extends NPC {

	private static final int BASE_TRAP = 11076;

	private String playerName;
	private int ticks;

	public MastyxTrap(String playerName, int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea);
		this.playerName = playerName;
	}

	@Override
	public void processNPC() {
		//Doesn't move or do anything so we don't process it.
		ticks++;
		if (ticks == 500) {
			finish();
			return;
		}
	}

	public String getPlayerName() {
		return playerName;
	}

	public int getTier() {
		return getId() - BASE_TRAP;
	}
}
