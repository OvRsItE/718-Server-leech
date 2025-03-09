package com.rs.game.world.entity.player.content.activities.trollinvasion;

import java.util.ArrayList;
import java.util.List;

import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;

@SuppressWarnings("serial")
public class TrollInvasionNPC extends NPC {

	private TrollInvasion controler;

	public TrollInvasionNPC(int id, Position tile, TrollInvasion controler) {
		super(id, tile, -1, 0, true, true);
		this.controler = controler;
		setForceMultiArea(true);
		setNoDistanceCheck(true);
	}

	@Override
	public void sendDeath(Entity source) {
		super.sendDeath(source);
		controler.addKill();
	}

	@Override
	public ArrayList<Entity> getPossibleTargets() {
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>(1);
		List<Integer> playerIndexes = World.getRegion(getRegionId())
				.getPlayerIndexes();
		if (playerIndexes != null) {
			for (int npcIndex : playerIndexes) {
				Player player = World.getPlayers().get(npcIndex);
				if (player == null || player.isDead() || player.isFinished()
						|| !player.isRunning())
					continue;
				possibleTarget.add(player);
			}
		}
		return possibleTarget;
	}

}
