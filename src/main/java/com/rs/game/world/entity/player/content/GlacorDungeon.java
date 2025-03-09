package com.rs.game.world.entity.player.content;

import com.rs.cores.tasks.WorldTask;
import com.rs.cores.tasks.WorldTasksManager;
import com.rs.game.map.WorldObject;
import com.rs.game.map.Position;
import com.rs.game.world.entity.player.Player;
import com.rs.game.world.entity.player.content.skills.Skills;
import com.rs.game.world.entity.updating.impl.Animation;
import com.rs.game.world.entity.updating.impl.Graphics;

/**
 * 
 * @author Annoying
 *
 */

public class GlacorDungeon {
	
	/*
	 * Player enters the Dungeon
	 */
	
	public static void EnterDungeonBoost(final Player player) {
		player.lock(1);
		player.animate(new Animation(10530));
		player.setNextGraphics(new Graphics(1340));
		int actualLevel = player.getSkills().getLevel(Skills.ATTACK);
		int realLevel = player.getSkills().getLevelForXp(Skills.ATTACK);
		int level = actualLevel > realLevel ? realLevel : actualLevel;
		player.getSkills().set(Skills.ATTACK,
				(int) (level + 25 + (realLevel * 0.22)));
		
		actualLevel = player.getSkills().getLevel(Skills.STRENGTH);
		realLevel = player.getSkills().getLevelForXp(Skills.STRENGTH);
		level = actualLevel > realLevel ? realLevel : actualLevel;
		player.getSkills().set(Skills.STRENGTH,
				(int) (level + 25 + (realLevel * 0.22)));

		actualLevel = player.getSkills().getLevel(Skills.DEFENCE);
		realLevel = player.getSkills().getLevelForXp(Skills.DEFENCE);
		level = actualLevel > realLevel ? realLevel : actualLevel;
		player.getSkills().set(Skills.DEFENCE,
				(int) (level + 25 + (realLevel * 0.22)));

		actualLevel = player.getSkills().getLevel(Skills.MAGIC);
		realLevel = player.getSkills().getLevelForXp(Skills.MAGIC);
		level = actualLevel > realLevel ? realLevel : actualLevel;
		player.getSkills().set(Skills.MAGIC, level + 27);

		actualLevel = player.getSkills().getLevel(Skills.RANGE);
		realLevel = player.getSkills().getLevelForXp(Skills.RANGE);
		level = actualLevel > realLevel ? realLevel : actualLevel;
		player.getSkills().set(Skills.RANGE,
				(int) (level + 20 + (Math.floor(realLevel / 5.2))));
	
	}
	
	/*
	 * Boolean for the coordinates
	 */
	
	public static boolean atGlacor(Position tile) {
		if ((tile.getX() >= 4164 && tile.getX() <= 4223)
				&& (tile.getY() >= 5698 && tile.getY() <= 5754))
			return true;
		return false;
	}
	
	/*
	 * Bariers
	 */
	
	public static void Barier(final Player player, WorldObject object) {
		 player.lock(1);
		 player.faceObject(object);
		 player.setNextGraphics(new Graphics(3008));
		 final Position toTile = new Position(player.getX() == 4205 ? 4206 : 4205, 5751, player.getZ());
		 player.addWalkSteps(player.getX() == 4205 ? 4206 : 4205, 5751, player.getZ(), false);
		 WorldTasksManager.schedule(new WorldTask() {

			@Override
			public void run() {
				player.setNextPosition(toTile);
			}	
			 
		 }, 1);
	}
}