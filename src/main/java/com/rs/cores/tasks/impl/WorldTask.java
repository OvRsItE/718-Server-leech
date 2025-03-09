package com.rs.cores.tasks.impl;

import com.rs.cores.tasks.Task;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.AchievementSystem;
import com.rs.utility.Logger;

public class WorldTask extends Task {

	private final static int TICK_DELAY = 1;

	public WorldTask() {
		super(TICK_DELAY, true, TickType.MAIN);
	}

	@Override
	protected void execute() {
		try {

			/*
			 * Process entities
			 */
			for (Player player : World.getPlayers()) {
				try {
					if (player == null || !player.hasStarted() || player.isFinished()) {
						continue;
					}
					player.processEntity();
				} catch (Exception e) {
					player.finish();
					Logger.handle(e);
				}
			}
			for (NPC npc : World.getNPCs()) {
				try {
					if (npc == null || npc.isFinished()) {
						continue;
					}
					npc.processEntity();
				} catch (Exception e) {
					Logger.handle(e);
				}
			}

			/*
			 * Update entities to players
			 */
			for (Player player : World.getPlayers()) {
				try {
					if (player == null || !player.hasStarted() || player.isFinished()) {
						continue;
					}
					player.getPackets().sendLocalPlayersUpdate();
					player.getPackets().sendLocalNPCsUpdate();
				} catch (Exception e) {
					player.finish();
					Logger.handle(e);
				}
			}

			/*
			 * Reset Players masks
			 */
			for (Player player : World.getPlayers()) {
				try {
					if (player == null || !player.hasStarted() || player.isFinished()) {
						continue;
					}
					player.resetMasks();
				} catch (Exception e) {
					player.finish();
					Logger.handle(e);
				}
			}

			/*
			 * Reset NPCs masks
			 */
			for (NPC npc : World.getNPCs()) {
				try {
					if (npc == null || npc.isFinished()) {
						continue;
					}
					npc.resetMasks();
				} catch (Exception e) {
					Logger.handle(e);
				}
			}

		} catch (Exception e) {
			Logger.handle(e);
		}
	}
}
