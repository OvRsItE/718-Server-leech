package com.rs.game.world.entity.npc.others;

import java.util.concurrent.TimeUnit;

import com.rs.cores.CoresManager;
import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.updating.impl.Animation;

@SuppressWarnings("serial")
public class Bork extends NPC {
	
	public static long deadTime;

	public Bork(int id, Position tile, int mapAreaNameHash, boolean canBeAttackFromOutOfArea, boolean spawned) {
		super(id, tile, mapAreaNameHash, 0, canBeAttackFromOutOfArea, spawned);
		setLureDelay(0);
		setForceAgressive(true);
	}

	@Override
	public void sendDeath(Entity source) {
		deadTime = System.currentTimeMillis() + (1000*60*60);
		resetWalkSteps();
		for (Entity e : getPossibleTargets()) {
			if (e instanceof Player) {
				final Player player = (Player) e;
				player.getInterfaceManager().sendInterface(693);
				player.getDialogueManager().startDialogue("DagonHai", 7137, player, 1);
				WorldTasksManager.schedule(new WorldTask() {
					@Override
					public void run() {
						player.stopAll();
					}
				}, 8);
			}
		}
		getCombat().removeTarget();
		animate(new Animation(getCombatDefinitions().getDeathEmote()));
		WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				drop();
				reset();
				setLocation(getSpawnPosition());
				finish();
				if (!isSpawned())
					setRespawnTask();
				stop();
			}
			
		}, 4);
	}
	
	@Override
	public void setRespawnTask() {
		if (!isFinished()) {
			reset();
			setLocation(getSpawnPosition());
			finish();
		}
		CoresManager.slowExecutor.schedule(new Runnable() {
			@Override
			public void run() {
				try {
					spawn();
				} catch (Exception e) {
					e.printStackTrace();
				} catch (Error e) {
					e.printStackTrace();
				}
			}
		}, 1,
			TimeUnit.HOURS);
	}
	
	public static String convertToTime() {
		String time = "You have to wait "+(getTime() == 0 ? "few more seconds" : getTime()+" mins")+" to kill bork again.";
		return time;
	}
	
	public static int getTime() {
		return (int) (deadTime-System.currentTimeMillis()/60000);
	}
	
	public static boolean atBork(Position tile) {
		if ((tile.getX() >= 3083 && tile.getX() <= 3120) && (tile.getY() >= 5522 && tile.getY() <= 5550))
			return true;
		return false;
	}
}
