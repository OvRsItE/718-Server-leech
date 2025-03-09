package com.rs.game.world.entity.npc.dungeonnering;

import com.rs.game.map.Position;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;

@SuppressWarnings("serial")
public class FrozenAdventurer extends NPC {

	private transient Player player;

	public FrozenAdventurer(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, canBeAttackFromOutOfArea, true);
	}

	@Override
	public void processNPC() {
		if (player == null || player.isDead() || player.hasFinished()) {
			finish();
			return;
		} /*else if (!player.getAppearence().isNPC()) {
			ToKashBloodChillerCombat.removeSpecialFreeze(player);
			finish();
			return;
		} */
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

	public Player getFrozenPlayer() {
		return player;
	}

}
