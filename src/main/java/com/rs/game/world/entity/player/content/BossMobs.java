package com.rs.game.world.entity.player.content;

import java.util.TimerTask;

import com.rs.cores.CoresManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.utility.Logger;

public class BossMobs {
	public static enum Delay {

		RIMMINGTON(3600000), HOME(1800000), FALADOR(7200000), VARROCK(
				6200000);

		public final int time;

		Delay(int time) {
			this.time = time;
		}

		public int getTime() {
			return time;
		}
	}

	public static boolean ongoingRimmington = false;
	
	public static boolean ongoingHome = false;
	
	public static boolean ongoingFalador = false;
	
	public static boolean ongoingVarrock = false;
	
	public static final void killEvents() {
		ongoingRimmington = false;
		ongoingHome = false;
		ongoingFalador = false;
		ongoingVarrock = false;
	}
	public static final void initGoblinRaids() {
		initRefresh();
		initAll();
	}

	public static final void initAll() {
		initRimmingtonRaid();
		initFaladorRaid();
		initHomeRaid();
		initVarrockRaid();
	}
	
	public static final void initRimmingtonRaid() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
@Override
			public void run() {
				try {
					initRefresh();
					World.sendWorldMessage("[<col=8B0000>Raid Event</col>] - Frost Dragons have invaded home!",
							false);
					World.spawnNPC(14413 , new Position(2527, 3088, 0), -1,
							0, true, true);
					World.spawnNPC(14413, new Position(2525, 3091, 0), -1,
							0, true, true);
					World.spawnNPC(14413, new Position(2530, 3091, 0), -1,
							0, true, true);
					ongoingRimmington = true;
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, Delay.RIMMINGTON.getTime());
	}

	public static final void initFaladorRaid() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
@Override
			public void run() {
				try {
					initRefresh();
					World.sendWorldMessage("[<col=8B0000>Raid Event</col>] - Black demons have raided Edgeville!",
							false);
					World.spawnNPC(10725, new Position(3166, 3512, 0), -1,
							0, true, true);
					World.spawnNPC(10725, new Position(3124, 3516, 0), -1,
							0, true, true);
					World.spawnNPC(10725, new Position(3127, 3519, 0), -1,
							0, true, true);
					ongoingFalador = true;
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, Delay.FALADOR.getTime());
	}

	public static final void initHomeRaid() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
@Override
			public void run() {
				try {
					initRefresh();
					World.sendWorldMessage("[<col=8B0000>Raid Event</col>] - Thugs have raided Varrock!",
							false);
					World.spawnNPC(6107, new Position(3218, 3427, 0), -1,
							0, true, true);
					World.spawnNPC(6107, new Position(3218, 3429, 0), -1,
							0, true, true);
					World.spawnNPC(6107, new Position(3217, 3432, 0), -1,
							0, true, true);
					ongoingHome = true;
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, Delay.HOME.getTime());
	}

	public static final void initVarrockRaid() {
		CoresManager.fastExecutor.schedule(new TimerTask() {
@Override
			public void run() {
				try {
					initRefresh();
					World.sendWorldMessage("[<col=8B0000>Raid Event</col>] - Skeleton thugs have raided Varrock!",
							false);
					World.spawnNPC(6107, new Position(3218, 3427, 0), -1,
							0, true, true);
					World.spawnNPC(6107, new Position(3218, 3427, 0), -1,
							0, true, true);
					World.spawnNPC(6107, new Position(3217, 3432, 0), -1,
							0, true, true);
					ongoingVarrock = true;
				} catch (Throwable e) {
					Logger.handle(e);
				}
			}
		}, 0, Delay.VARROCK.getTime());
	}

	public static final void initRefresh() {
		for (NPC n : World.getNPCs()) {
			if (n == null || n.getId() != 3264)
				continue;
			n.sendDeath(n);
		}
		killEvents();
	}
}
