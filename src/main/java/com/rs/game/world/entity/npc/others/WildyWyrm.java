package com.rs.game.world.entity.npc.others;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;
import com.rs.utility.Utils;

@SuppressWarnings("serial")
public class WildyWyrm extends NPC {

	private int stompId;

	public WildyWyrm(int id, Position tile, int mapAreaNameHash,
                     boolean canBeAttackFromOutOfArea) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, true);
		setCombatLevel(59999);
		setName("wakaakakkaka");
		setRun(true);
		setForceMultiAttacked(true);
		stompId = id;
	}

	@Override
	public void processNPC() {
		super.processNPC();
		if (isDead())
			return;
		if (getId() != stompId && !isCantInteract() && !isUnderCombat()) {
			animate(new Animation(12796));
			setCantInteract(true);
			WorldTasksManager.schedule(new WorldTask() {
				@Override
				public void run() {
					transformIntoNPC(2417);
					setCantInteract(false);
				}
			});
		}
	}

	@Override
	public void reset() {
		setNPC(stompId);
		super.reset();
	}

	public static void handleStomping(final Player player, final NPC npc) {
		if (npc.isCantInteract())
			return;
		if (!npc.isAtMultiArea() || !player.isAtMultiArea()) {
			if (player.getAttackedBy() != npc
					&& player.getAttackedByDelay() > Utils.currentTimeMillis()) {
				player.getPackets().sendGameMessage(
						"You are already in combat.");
				return;
			}
			if (npc.getAttackedBy() != player
					&& npc.getAttackedByDelay() > Utils.currentTimeMillis()) {
				if (npc.getAttackedBy() instanceof NPC) {
					npc.setAttackedBy(player); // changes enemy to player,
					// player has priority over
					// npc on single areas
				} else {
					player.getPackets().sendGameMessage(
							"That npc is already in combat.");
					return;
				}
			}
		}
		switch (npc.getId()) {
		case 2417:
			if (player.getSkills().getLevel(18) < 1) {
				player.getPackets()
						.sendGameMessage(
								"You need at least a slayer level of 93 to fight this.");
				return;
			}
			break;
		default:
			return;
		}
		player.animate(new Animation(4278));
		WorldTasksManager.schedule(new WorldTask() {
			@Override
			public void run() {
				npc.animate(new Animation(12795));
				npc.transformIntoNPC(3334);
				npc.setTarget(player);
				npc.setAttackedBy(player);
				stop();
			}

		}, 1, 2);
	}
	@Override
	public void sendDeath(Entity source) {
		switch (getId()) {
		case 3334:
		setNextGraphics(new Graphics(2924 + getSize()));
		transformIntoNPC(2417);
		super.sendDeath(source);
		}
	}
}

