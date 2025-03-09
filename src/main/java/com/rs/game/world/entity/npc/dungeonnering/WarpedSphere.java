package com.rs.game.world.entity.npc.dungeonnering;


import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.dungeoneering.DungeonManager;
import com.rs.game.world.entity.player.content.skills.dungeoneering.RoomReference;
import com.rs.game.world.entity.player.content.skills.magic.Magic;
import com.rs.game.world.entity.updating.impl.Hit;

@SuppressWarnings("serial")
public class WarpedSphere extends DungeonNPC {

	private static final int[][] ORB_TELEPORT_LOCATIONS =
		{{ 0, 0 },
		{ 0, 0 },
		{ 13, 4 },
		{ 11, 12 },
		{ 3, 4 },
		{ 3, 12 } },

		PLAYER_TELEPORT_LOCATIONS =
	{{ 0, 0 },
		{ 0, 0 },
		{ 6, 11 },
		{ 10, 4 },
		{ 10, 11 },
		{ 10, 4 } };

	private final RoomReference reference;
	private int stage;

	public WarpedSphere(RoomReference reference, int id, Position tile, DungeonManager manager, double multiplier) {
		super(id, tile, manager, multiplier);
		this.reference = reference;
		stage = -1;//Gotta follow warmonger
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (stage > 0) {
		//	if (hasWalkSteps()) {
				final int[] TELEPORT_TILE = PLAYER_TELEPORT_LOCATIONS[stage];
				for (Player player : getManager().getParty().getTeam()) {
					if (!player.isLocked() && player.matches(this)) {
						player.applyHit(new Hit(this, 100, Hit.HitLook.REFLECTED_DAMAGE));
						Magic.sendTeleportSpell(player, 13493, 13494, 2437, 2435, 0, 0, getManager().getTile(reference, TELEPORT_TILE[0], TELEPORT_TILE[1]), 6, false, 0);
					}
				}
		//	}
		}

		if (!hasWalkSteps()) {
			boolean can = false;
			if (Math.random() > 0.59564)
				can = true;

			if (can) {
				int moveX = (int) Math.round(Math.random() * 10.0 - 5.0);
				int moveY = (int) Math.round(Math.random() * 10.0 - 5.0);
				addWalkSteps(getSpawnPosition().getX() + moveX, getSpawnPosition().getY() + moveY, 5, true);
			}
		}
	}

	public void nextStage() {
		stage++;
		final int[] TELEPORT_TILE = ORB_TELEPORT_LOCATIONS[stage];
		if (TELEPORT_TILE[0] == 0)
			return;
		Position nextTile = getManager().getTile(reference, TELEPORT_TILE[0], TELEPORT_TILE[1]);
		setNextPosition(nextTile);
	}
}
