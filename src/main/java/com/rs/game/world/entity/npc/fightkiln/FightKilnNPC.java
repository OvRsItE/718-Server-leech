package com.rs.game.world.entity.npc.fightkiln;

import java.util.ArrayList;
import java.util.List;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.Position;
import com.rs.game.world.World;
import com.rs.game.world.entity.Entity;
import com.rs.game.world.entity.npc.NPC;
import com.rs.game.world.entity.npc.combat.NPCCombatDefinitions;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.controller.impl.FightKiln;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;

@SuppressWarnings("serial")
public class FightKilnNPC extends NPC {

	private FightKiln controler;

	public FightKilnNPC(int id, Position tile, FightKiln controler) {
		super(id, tile, -1, 0, true, true);
		setForceMultiArea(true);
		setNoDistanceCheck(true);
		this.controler = controler;
	}
	
	
	private int getDeathGfx() {
		switch(getId()) {
		case 15201: return 2926;
		case 15202: return 2927;
		case 15203: return 2957;
		case 15213:
		case 15214:
		case 15204: return 2928;
		case 15205: return 2959;
		case 15206:
		case 15207:  return 2929;
		case 15208: 
		case 15211:
		case 15212: return 2973;
		default: return 2926;
		}
	}

	@Override
	public void sendDeath(Entity source) {
		final NPCCombatDefinitions defs = getCombatDefinitions();
		resetWalkSteps();
		getCombat().removeTarget();
		animate(null);
		controler.checkCrystal();
		setNextGraphics(new Graphics(getDeathGfx()));
		WorldTasksManager.schedule(new WorldTask() {
			int loop;

			@Override
			public void run() {
				if (loop == 0) {
					animate(new Animation(defs.getDeathEmote()));
				} else if (loop >= defs.getDeathDelay()) {
					reset();
					finish();
					controler.removeNPC();
					stop();
				}
				loop++;
			}
		}, 0, 1);
	}

	@Override
	public ArrayList<Entity> getPossibleTargets() {
		ArrayList<Entity> possibleTarget = new ArrayList<Entity>(1);
		List<Integer> playerIndexes = World.getRegion(getRegionId()).getPlayerIndexes();
		if(playerIndexes != null) {
			for (int npcIndex : playerIndexes) {
				Player player = World.getPlayers().get(npcIndex);
				if (player == null
						|| player.isDead()
						|| player.isFinished()
						|| !player.isRunning())
					continue;
				possibleTarget.add(player);
			}
		}
		return possibleTarget;
	}

	
	
	@Override
	public double getMagePrayerMultiplier() {
		return 0.1;
	}
	
	@Override
	public double getRangePrayerMultiplier() {
		return 0.1;
	}
	
	@Override
	public double getMeleePrayerMultiplier() {
		return 0.1;
	}
	
}

