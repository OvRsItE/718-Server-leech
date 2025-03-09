package com.rs.cores;

import com.rs.Constants;
import com.rs.cores.coroutines.CoroutineWorker;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.world.World;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.utility.Logger;
import com.rs.utility.Utils;
import kilim.Pausable;
import lombok.Getter;

public final class WorldThread extends Thread {

	public static int WORLD_CYCLE = 0;
	public static volatile long getLastCycleCtmWORLD_CYCLE;
	
	protected WorldThread() {
		setPriority(Thread.MAX_PRIORITY);
		setName("World Thread");
	}

	@Override
	public final void run() {
		while (!CoresManager.shutdown) {
			WORLD_CYCLE++; //made the cycle update at begin instead of end cuz at end theres 600ms then to next cycle
			long currentTime = Utils.currentTimeMillis();
			try {

				currentStage = Stage.LOGIC;
				CoroutineWorker.tick();
				WorldTasksManager.processTasks();

				currentStage = Stage.PROCESS;
				for (Player player : World.getPlayers()) {
					if (player == null || !player.hasStarted()
							|| player.isFinished())
						continue;
					if (currentTime - player.getPacketsDecoderPing() > Constants.MAX_PACKETS_DECODER_PING_DELAY && player.getSession().getChannel().isOpen())
						player.getSession().getChannel().close();
					player.processEntity();
				}

				for (NPC npc : World.getNPCs()) {
					if (npc == null || npc.isFinished())
						continue;
					npc.processEntity();
				}

			} catch (Throwable e) {
				Logger.handle(e);
			}
			try {
				// System.out.print(" ,NPCS PROCESS: "+(Utils.currentTimeMillis()-debug));
				// debug = Utils.currentTimeMillis();

				currentStage = Stage.UPDATE;
				for (Player player : World.getPlayers()) {
					if (player == null || !player.hasStarted()
							|| player.isFinished())
						continue;
					player.getPackets().sendLocalPlayersUpdate();
					player.getPackets().sendLocalNPCsUpdate();
				}
				// System.out.print(" ,PLAYER UPDATE: "+(Utils.currentTimeMillis()-debug)+", "+World.getPlayers().size()+", "+World.getNPCs().size());
				// debug = Utils.currentTimeMillis();
				for (Player player : World.getPlayers()) {
					if (player == null || !player.hasStarted()
							|| player.isFinished())
						continue;
					player.resetMasks();
				}
				for (NPC npc : World.getNPCs()) {
					if (npc == null || npc.isFinished())
						continue;
					npc.resetMasks();
				}
			} catch (Throwable e) {
				Logger.handle(e);
			}
			// System.out.println(" ,TOTAL: "+(Utils.currentTimeMillis()-currentTime));
			LAST_CYCLE_CTM = Utils.currentTimeMillis();
			long sleepTime = Constants.WORLD_CYCLE_TIME + currentTime
					- LAST_CYCLE_CTM;
			if (sleepTime <= 0)
				continue;
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				Logger.handle(e);
			}
		}
	}

	public enum Stage {
		LOGIC,
		PROCESS,
		UPDATE
	}

	public static boolean isPast(Stage stage) {
		return currentStage.ordinal() > stage.ordinal();
	}

	public static Stage currentStage = Stage.LOGIC;
	public static long LAST_CYCLE_CTM;

}
