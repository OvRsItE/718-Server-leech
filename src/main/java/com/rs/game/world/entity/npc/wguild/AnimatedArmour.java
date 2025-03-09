package com.rs.game.world.entity.npc.wguild;

import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;

public class AnimatedArmour extends NPC {

	public Player playerCon;

	public void setPlayerCon(Player con) {
		playerCon = con;
	}

	public Player getPlayerCon() {
		return playerCon;
	}

	public AnimatedArmour(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea,
                          boolean spawned) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
	}

	@Override
	public void sendDeath(Entity source) {
		Player other = (Player) source;
		super.sendDeath(source);
		other.getTemporaryAttributtes().put("killednpc", getTokensForNpcId());
	}

	public int getTokensForNpcId() {
		switch (this.getId()) {
		case 4278:
			return 20;
		case 4279:
			return 25;
		case 4280:
			return 30;
		case 4282:
			return 35;
		case 4283:
			return 40;
		case 4284:
			return 100;
		}
		return 0;
	}

}